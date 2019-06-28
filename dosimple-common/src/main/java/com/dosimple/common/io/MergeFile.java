package com.dosimple.common.io;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : dosimple
 */
public class MergeFile {
    private static final Logger logger = LoggerFactory.getLogger(MergeFile.class.getName());
    private List<String> filePaths;
    private String sourceFolder;
    private String destFilePath;
    private IHandle handle;
    private static final int BUFF_LENGTH = 1024 * 1024;

    public MergeFile(List<String> filePaths, String sourceFolder, String destFilePath, IHandle handle) {
        this.handle = handle == null ? new IHandle() {
            @Override
            public void handle(byte[] bytes, Integer threadNo) {

            }
        } : handle;
        this.filePaths = filePaths;
        this.sourceFolder = sourceFolder;
        this.destFilePath = destFilePath;
    }

    public String merge() {
        long old = System.currentTimeMillis();
        if (StringUtils.isNotBlank(sourceFolder)) {
            File source = new File(sourceFolder);
            String[] files = source.list();
            if (null == files) {
                logger.error("files not exists");
                return null;
            }
            filePaths = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                filePaths.add(source.getAbsolutePath() + File.separator + files[i]);
            }
        }
        if (null == filePaths || filePaths.size() == 0) {
            logger.error("source folder not have files");
            return null;
        }

        File destFile = new File(destFilePath);
        try {
            destFile.delete();
            if (!destFile.createNewFile()) {
                logger.error("create dest file fail");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        long startPos = 0;
        for (int i = 0; i < filePaths.size(); i++) {
            if (i == 0) {
                startPos = 0;
            } else{
                File file = new File(filePaths.get(i-1));
                startPos += file.length();
            }
            try {
                MergeFileThread mergeFileThread = new MergeFileThread(new RandomAccessFile(filePaths.get(i), "r")
                        , new RandomAccessFile(destFile, "rw"), startPos, i);
                executorService.execute(mergeFileThread);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.debug("merge time use >>> " + (System.currentTimeMillis() - old));
        return destFilePath;
    }

    private class MergeFileThread implements Runnable {
        private RandomAccessFile read;
        private RandomAccessFile write;
        private int threadNo;
        private long startPos;//合并文件的上一个文件的大小

        public MergeFileThread(RandomAccessFile read, RandomAccessFile write, long startPos, int threadNo) {
            this.read = read;
            this.write = write;
            this.startPos = startPos;
            this.threadNo = threadNo;
        }

        @Override
        public void run() {
            try {
                write.seek(startPos);
                read.seek(0);
                while (true) {
                    byte[] readBytes = new byte[BUFF_LENGTH];
                    int hasRead = read.read(readBytes);
                    if (hasRead < 0) {
                        break;
                    }
                    if (hasRead < BUFF_LENGTH) {
                        byte[] lastBytes = new byte[hasRead];
                        System.arraycopy(readBytes, 0, lastBytes, 0, hasRead);
                        write.write(lastBytes);
                        handle.handle(lastBytes, threadNo);
                    } else {
                        write.write(readBytes);
                        handle.handle(readBytes, threadNo);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    read.close();
                    write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
