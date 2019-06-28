package com.dosimple.common.util;

import com.dosimple.common.io.ReadFile;
import com.dosimple.common.io.IHandle;
import com.dosimple.common.io.MergeFile;
import com.dosimple.common.io.SplitFile;

import java.util.List;

/**
 * @author dosimple
 */
public class FileUilts {
    /**
     * 分割文件
     * @param sourceFilePath 源文件路径
     * @param countFile 分割多少文件
     * @param parentFolder 父文件夹
     * @return
     */
    public static String split(String sourceFilePath, Integer countFile, String parentFolder, IHandle handle) {
        SplitFile splitFile = new SplitFile(sourceFilePath, parentFolder, countFile, handle);
        return splitFile.split();
    }

    /**
     * 分割文件
     * 分割后会当前目录创建一个UUID的文件夹，分割文件在该文件夹内
     * @param sourceFilePath 源文件路径
     * @return
     */
    public static String split(String sourceFilePath, IHandle handle) {
        SplitFile splitFile = new SplitFile(sourceFilePath, null, null, handle);
        return splitFile.split();
    }

    /**
     * 读取文件
     * @param sourceFilePath 文件路径
     * @param IHandle 辅助类
     * @return
     */
    public static void read(String sourceFilePath, IHandle IHandle) {
        ReadFile readFile = new ReadFile(IHandle, null, sourceFilePath, null, Boolean.FALSE, Boolean.FALSE);
        readFile.read();
    }
    /**
     * 读取文件并根据cpuUseRate来允许指定的线程数
     * @param sourceFilePath 文件路径
     * @param IHandle 辅助类
     * @return
     */
    public static void read(String sourceFilePath, Integer cpuUseRate, IHandle IHandle) {
        ReadFile readFile =  new ReadFile(IHandle, null, sourceFilePath, cpuUseRate, Boolean.FALSE, Boolean.FALSE);
        readFile.read();
    }

    /**
     * 读取文本含有多少指定的词,并根据cpuUseRate来允许指定的线程数
     * @param sourceFilePath 文件路径
     * @param regex 匹配词的正则
     * @param cpuUseRate CPU使用率
     * 判断是否对截断部分的字符进行组合操作，比如
       regex=[a-zA-Z]+
       文件中 this is apple由于多线程分割后可能截断为  th,is is, app,le
       如果这里设置为TRUE，则会将截断部分合并为 this is apple
     * @param calculateCutWords
     * @param IHandle 辅助类
     * @return
     */
    public static Long countWords(String sourceFilePath, String regex, Integer cpuUseRate, Boolean calculateCutWords, IHandle IHandle) {
        ReadFile readFileWords = new ReadFile(IHandle, regex, sourceFilePath, cpuUseRate, calculateCutWords, Boolean.TRUE);
        readFileWords.read();
        return readFileWords.getCountWords().get();
    }
    /**
     * 读取文本含有多少指定的词
     * @param sourceFilePath 文件路径
     * @param regex 匹配词的正则
     * @param IHandle 辅助类
     * @return
     */
    public static Long countWords(String sourceFilePath, String regex, IHandle IHandle) {
        ReadFile readFileWords = new ReadFile(IHandle, regex, sourceFilePath, null, Boolean.TRUE, Boolean.TRUE);
        readFileWords.read();
        return readFileWords.getCountWords().get();
    }

    /**
     * 合并文件
     * @param sourceFilePaths 合并的文件。从list的第一个开始合并到最后
     * @param destFilePath 目标文件
     * @return
     */
    public static String merge(List<String> sourceFilePaths, String destFilePath, IHandle handle) {
        MergeFile mergeFile = new MergeFile(sourceFilePaths, null, destFilePath, handle);
        return mergeFile.merge();
    }
    /**
     * 合并文件
     * @param sourceFolder 合并的文件的文件夹
     * @param destFilePath 目标文件
     * @return 合并后的文件名
     */
    public static String merge(String sourceFolder, String destFilePath, IHandle handle) {
        MergeFile mergeFile = new MergeFile(null, sourceFolder, destFilePath, handle);
        return mergeFile.merge();
    }

}
