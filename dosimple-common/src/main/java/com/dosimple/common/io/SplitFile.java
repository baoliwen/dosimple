package com.dosimple.common.io;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @date: 2019/4/25 0025 下午 5:29
 * @author dosimple
 * 多线程分割文件
 */
public class SplitFile {
    private static final Logger logger = LoggerFactory.getLogger(SplitFile.class.getName());
    private String filePath;
    private String parentFolderPath;
    private static final int BUFF_LENGTH = 1024 * 1024;
    //分割文件数量
    private Integer countFile = 10;
    private IHandle handle;
    public SplitFile(String filePath, String parentFolderPath, Integer countFile, IHandle handle) {
        this.handle = handle == null ? new IHandle() {
            @Override
            public void handle(byte[] bytes, Integer threadNo) {

            }
        } : handle;
        this.filePath = filePath;
        this.parentFolderPath = parentFolderPath;
        this.countFile = countFile == null ? 10 : countFile;
    }
    /**
     * 分割文件
     *
     * @return 分割文件的文件夹路径
     */
    public String split() {
        long old = System.currentTimeMillis();
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("不存在该文件夹");
            return null;
        }
        String fileType = file.getName().substring(file.getName().lastIndexOf("."));
        // 分割后各文件的大小
        long fileLength = file.length();
        long fileBlockSize = (long) Math.ceil((double) fileLength / (double)countFile);
        if (fileLength < BUFF_LENGTH) {
            countFile = 1;
            fileBlockSize = BUFF_LENGTH;
        }
        String parentFold = StringUtils.isNotBlank(parentFolderPath) ? parentFolderPath : file.getParent() + UUID.randomUUID();
        File parentFile = new File(parentFold);
        // 创建输出的文件夹
        boolean createFlag = parentFile.mkdirs();
        if (!createFlag) {
            logger.error("创建文件夹失败");
            return null;
        }
        CountDownLatch doneSignal = new CountDownLatch(countFile);
        boolean hasException = false;//如果发生异常，则删除创建的文件
        for (int i = 0; i < countFile; i++) {
            String fileName = parentFold + File.separator + i + fileType;
            File childFile = new File(fileName);
            if (!childFile.exists()) {
                try {
                    if (!childFile.createNewFile()) return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    hasException = true;
                }
            }
            try {
                SplitFileThread splitFile = new SplitFileThread(i, new RandomAccessFile(filePath, "r"), fileBlockSize
                        , fileLength, new BufferedOutputStream(new FileOutputStream(childFile, true)), doneSignal);
                Thread thread = new Thread(splitFile);
                thread.setName("splitFileNo" + i);
                thread.start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                hasException = true;
            }
        }
        try {
            doneSignal.await();//等待所有线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
            hasException = true;
        }
        if (hasException) {
            logger.error("发生异常，删除所有文件");
            for (File file1 : parentFile.listFiles()) {
                file1.delete();
            }
            parentFile.delete();
            return null;
        }
        logger.debug("split time use >>> " + (System.currentTimeMillis() - old));
        return parentFold;
    }

    private class SplitFileThread implements Runnable {
        private int threadNo;
        private RandomAccessFile raf;
        private long fileBlockSize;
        private long fileLength;
        private OutputStream out;
        //用于多线程执行完后让主线程继续执行的辅助
        private CountDownLatch doneSignal;

        private SplitFileThread(int threadNo, RandomAccessFile raf, long fileBlockSize, long fileLength, OutputStream out, CountDownLatch doneSignal) {
            this.threadNo = threadNo;
            this.raf = raf;
            this.fileBlockSize = fileBlockSize;
            this.fileLength = fileLength;
            this.out = out;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            long startPos = threadNo * fileBlockSize;//每一次的指针起始位置
            if (startPos > fileLength) {//起始位置如果大于文件总大小，则线程停止
                doneSignal.countDown();
                return;
            }
            if (startPos + fileBlockSize > fileLength) {
                fileBlockSize = fileLength - startPos;
            }
            try {
                raf.seek(startPos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean first = true;
            try {
                while (true) {
                    if (fileBlockSize <= 0) {
                        break;
                    }
                    if (!first) {
                        startPos += BUFF_LENGTH;
                    }
                    raf.seek(startPos);
                    byte[] buff = new byte[fileBlockSize < BUFF_LENGTH ? (int) fileBlockSize : BUFF_LENGTH];//每一次读取的数据量
                    int hasRead = raf.read(buff);
                    if (hasRead < 0) {
                        raf.close();
                        break;
                    }
                    if (hasRead < buff.length) {
                        byte[] lastBytes = new byte[hasRead];
                        System.arraycopy(buff, 0, lastBytes, 0, hasRead);
                        handle.handle(lastBytes, threadNo);
                    } else {
                        handle.handle(buff, threadNo);
                    }
                    out.write(buff);
                    out.flush();
                    fileBlockSize -= (fileBlockSize < BUFF_LENGTH ? (int) fileBlockSize : BUFF_LENGTH);
                    first = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    out.close();
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                doneSignal.countDown();
            }
        }
    }


}
