package com.dosimple.common.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dosimple
 * 多线程读文档
 */
public class ReadFile {
    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class.getName());
    private AtomicLong countWords = new AtomicLong(0);
    private ConcurrentHashMap<Long, String> cutWordsMap = new ConcurrentHashMap<>();
    //定义字节数组（取水的竹筒）的长度
    private int buffLength = 1024 * 1024;
    private IHandle handle;
    //字符匹配正则
    private String regex;
    //filePath   文件路径
    private String filePath;
    //CPU核数使用率,比如4核，使用率是50，则会创建2个线程读取文件。使用率为100时，创建 4*2 的线程读取文件
    private int cpuUseRate;
    /*
    判断是否对截断部分的字符进行组合操作，比如
    regex=[a-zA-Z]+
    文件中 this is apple由于多线程分割后可能截断为  th,is is, app,le
    如果这里设置为TRUE，则会将截断部分合并为 this is apple
     */
    private Boolean calculateCutWords = Boolean.TRUE;
    //是否计算文件中正则匹配单词数量
    private Boolean calculateWords = Boolean.FALSE;

    public ReadFile(IHandle handle, String regex, String filePath, Integer cpuUseRate, Boolean calculateCutWords
            , Boolean calculateWords) {
        this.handle = handle == null ? new IHandle() {
            @Override
            public void handle(byte[] bytes, Integer threadNo) {

            }
        } : handle;
        this.regex = regex;
        this.filePath = filePath;
        this.cpuUseRate = cpuUseRate == null ? 100 : cpuUseRate;
        this.calculateCutWords = calculateCutWords == null ? Boolean.TRUE : calculateCutWords;
        this.calculateWords = calculateWords == null ? Boolean.FALSE : calculateWords;
    }

    public AtomicLong getCountWords() {
        return countWords;
    }

    public void read() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int availableCountThread;
        if (cpuUseRate >= 100 || cpuUseRate <= 0) {
            availableCountThread = Runtime.getRuntime().availableProcessors() * 2;
        } else {
            availableCountThread = (int) Math.ceil((double) availableProcessors * cpuUseRate / 100);
        }
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("file not exists");
            return;
        }
        long fileLength = file.length();
        if (file.length() < buffLength) {//文件小于1M，不需要多线程
            buffLength = (int) file.length();
            availableCountThread = 1;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(availableCountThread);
        long old = System.currentTimeMillis();
        for (int i = 0; i < availableCountThread; i++) {
            try {
                ReadThread thread = new ReadThread(fileLength, i, availableCountThread, new RandomAccessFile(filePath, "r"));
                executorService.execute(thread);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        CalculateCutWordsThread calculateCutWords1 = new CalculateCutWordsThread();
        CalculateCutWordsThread calculateCutWords2 = new CalculateCutWordsThread();
        if (calculateWords) {
            new Thread(calculateCutWords1).start();
            new Thread(calculateCutWords2).start();
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.debug("read file finish time use>>> {}", (System.currentTimeMillis() - old));
        while (calculateWords && cutWordsMap.size() > 0) {
            Set<Map.Entry<Long, String>> entries = cutWordsMap.entrySet();
            Iterator<Map.Entry<Long, String>> iteratorMap = entries.iterator();
            while (iteratorMap.hasNext()) {
                Map.Entry<Long, String> entry = iteratorMap.next();
                if (null == cutWordsMap.get(entry.getKey() - 1) && null == cutWordsMap.get(entry.getKey() + 1)) {
                    Matcher slashMatcher = Pattern.compile(regex).matcher(entry.getValue());
                    calculateWords(slashMatcher);
                    iteratorMap.remove();
                }
            }
        }
        calculateCutWords1.setStop(true);
        calculateCutWords2.setStop(true);
        logger.debug("time use>>> {}", (System.currentTimeMillis() - old));
    }

    /**
     * 读取文件
     */
    private class ReadThread implements Runnable {
        private int threadNo;
        //开启的总线程数
        private int countThread;
        //文件长度
        private long fileLength;
        private RandomAccessFile raf;

        private ReadThread(long fileLength, int threadNo, int countThread, RandomAccessFile raf) {
            this.fileLength = fileLength;
            this.threadNo = threadNo;
            this.countThread = countThread;
            this.raf = raf;
        }

        @Override
        public void run() {
            long startPos = threadNo * buffLength;//每一次的指针起始位置
            if (startPos > fileLength) {//起始位置如果大于文件总大小，则线程停止
                return;
            }
            String result;
            try {
                raf.seek(startPos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean first = true;//是否是第一次，如果是第一次，在循环体中不再跳跃指针
            try {
                while (true) {

                    if (!first) {
                        startPos += buffLength * countThread;
                        if (startPos >= fileLength) {//起始位置已经大于总文件大小
                            break;
                        }
                        raf.seek(startPos);
                    }
                    byte[] buff = new byte[buffLength];//每一次读取的数据量
                    int hasRead = raf.read(buff);
                    if (hasRead < 0) {
                        raf.close();
                        break;
                    }
                    if (!calculateWords) {
                        if (hasRead < buffLength) {
                            byte[] lastBytes = new byte[hasRead];
                            System.arraycopy(buff, 0, lastBytes, 0, hasRead);
                            handle.handle(lastBytes, null);
                        } else {
                            handle.handle(buff, null);
                        }
                    } else {
                        result = new String(buff, "UTF-8");
                        /*
                        1.获取获取记录的 正则匹配的 开头位置和结尾位置。
                        2.截取开头结尾，放入合并单词比较map中
                         */
                        Matcher slashMatcher = Pattern.compile(regex).matcher(result);
                        slashMatcher = putCutWords(startPos, result, slashMatcher);
                        calculateWords(slashMatcher);
                    }

                    first = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        private Matcher putCutWords(long startPos, String result, Matcher slashMatcher) {
            if (calculateCutWords) {
                int mIdx = 0;
                int firstPos = 0;
                int endPos = 0;
                while (slashMatcher.find()) {
                    if (mIdx == 1) {
                        firstPos = slashMatcher.start();
                    } else {
                        endPos = slashMatcher.start();
                    }
                    mIdx++;
                }
                if (startPos == 0) {//不取第一条记录的开头单词
                    firstPos = 0;
                }
                if (startPos + buffLength >= fileLength) {//不取最后一条记录的结尾单词
                    endPos = result.length();
                }
                //被截取块的开头字符串
                cutWordsMap.put(startPos, result.substring(0, firstPos));
                //被截取块的结尾字符串
                cutWordsMap.put(startPos + buffLength - 1, result.substring(endPos, result.length()));
                slashMatcher = Pattern.compile(regex).matcher(result.substring(firstPos, endPos));
            }
            return slashMatcher;
        }
    }

    /**
     * 计算截断的单词数量
     */
    private class CalculateCutWordsThread implements Runnable {
        private boolean stop = false;

        private void setStop(boolean stop) {
            this.stop = stop;
        }

        @Override
        public void run() {
            while (!stop) {
                Map<Long, String> tempMap = new HashMap<>(cutWordsMap);
                Set<Map.Entry<Long, String>> tempEntries = tempMap.entrySet();
                Iterator<Map.Entry<Long, String>> it = tempEntries.iterator();
                synchronized (CalculateCutWordsThread.class) {
                    while (it.hasNext()) {
                        Map.Entry<Long, String> entry = it.next();
                        String head = cutWordsMap.get(entry.getKey() - 1);
                        String current = cutWordsMap.get(entry.getKey());
                        String end = cutWordsMap.get(entry.getKey() + 1);
                        String result = null;
                        if (null != head) {
                            result = head + current;
                            cutWordsMap.remove(entry.getKey() - 1);
                            cutWordsMap.remove(entry.getKey());
                        } else if (end != null) {
                            result = current + end;
                            cutWordsMap.remove(entry.getKey() + 1);
                            cutWordsMap.remove(entry.getKey());
                        }
                        if (null != result) {
                            Matcher slashMatcher = Pattern.compile(regex).matcher(result);
                            calculateWords(slashMatcher);
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算符合正则的单词数量
     *
     * @param slashMatcher
     */
    private void calculateWords(Matcher slashMatcher) {
        int size = 0;
        while (slashMatcher.find()) {
            size++;
            handle.handle(slashMatcher.group().getBytes(), null);
        }
        countWords.addAndGet(size);
    }
}
