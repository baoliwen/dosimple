package com.dosimple.common.io;

/**
 * @date: 2019/4/26 0026 上午 11:15
 * @author dosimple
 */
public interface IHandle {
    /**
     * 可以将bytes转换为字符串操作
     * @param bytes 读取的字节
     * @param threadNo 线程编号
     */
    void handle(byte[] bytes, Integer threadNo);
}
