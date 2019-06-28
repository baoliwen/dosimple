package com.dosimple.common;

import com.dosimple.common.io.IHandle;
import com.dosimple.common.util.FileUilts;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @date: 2019/4/25 0025 下午 6:09
 * @author dosimple
 */
@SpringBootTest
public class HandleFileTests {

    @Test
    public void countFileWordsFile0() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite.txt", "[a-zA-Z0-9]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }
    @Test
    public void readFile() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                atomicLong.addAndGet(1);
            }
        };
        FileUilts.read("E:\\testWrite.txt", h);
        System.out.println(atomicLong.get());
    }

    @Test
    public void countFileWordsFile1() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite1.txt", "[a-zA-Z0-9]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }

    @Test
    public void countFileWordsFile2() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite22.txt", "[a-zA-Z0-9]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }

    @Test
    public void countFileWordsFile4() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite4.txt", "[a-zA-Z0-9]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }

    @Test
    public void countFileWordsFile5() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite5.txt", "[a-zA-Z0-9]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }
    @Test
    public void countFileWordsFile6() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite6.txt", "[\u4e00-\u9fa5]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }
    @Test
    public void countFileWordsFile7() {
        AtomicLong atomicLong = new AtomicLong(0);
        IHandle h = new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {
                if (new String(bytes).contains("aaaaaa")) {
                    atomicLong.addAndGet(1);
                }
            }
        };
        Long number = FileUilts.countWords("E:\\testWrite7.txt", "[\u4e00-\u9fa5]+"
                , 100,true, h);
        System.out.println("atomicLong>>>>>" + atomicLong);
        System.out.println("文件中含有单词数量>>>>" + number);
    }
    @Test
    public void splitFile() {
        String path = FileUilts.split("E:\\samsung-notes-3-0-01-18.apk", new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {

            }
        });
        System.out.println("父文件夹路径 >>> " + path);
    }
    @Test
    public void splitFile2() {
        String path = FileUilts.split("E:\\testWrite2.txt", 10, "E:\\11111111111111", new IHandle() {
           @Override
                    public void handle(byte[] bytes, Integer threadNo) {

            }
        });
        System.out.println("父文件夹路径 >>> " + path);
    }

    @Test
    public void write() throws IOException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        File f = new File("E:\\testWrite11.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter out = new FileWriter(f);
        for (long i = 1; i <= 500000L; i++) {
            String ss = RandomStringUtils.randomAlphabetic(4, 10);
            ss = "aaaaaaa>>>>>>" + i + "<<<<<<aaaaaaa";
            if (i % 10 == 0) {
                out.write(ss + "\n");
            } else {
                out.write(ss + " ");
            }
        }
        out.close();
    }


    @Test
    public void listFiles() {
        File file = new File("E:\\3333");
        System.out.println(file.list() == null);
    }

    @Test
    public void mergeFile() {
        System.out.println(FileUilts.merge("E:\\7e49d24c-a193-467b-9fee-d555f8b4f83d"
                , "E:\\11111111111111.apk", new IHandle() {
                    @Override
                    public void handle(byte[] bytes, Integer threadNo) {

                    }
                }));
    }

}
