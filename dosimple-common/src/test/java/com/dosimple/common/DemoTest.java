package com.dosimple.common;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date: 2019/4/26 0026 下午 6:34
 * @author dosimple
 */
@SpringBootTest
public class DemoTest {

    @Test
    public void test() {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]+");
        System.out.println(Arrays.asList(pattern.split("第三方哈克斯交党费哈克斯京东方哈萨克剪短发哈看时间点合法会计师电话费")));
        System.out.println("dsfs,gh".matches("\\b"));
        System.out.println("hello>>>"+"hello".matches("[\u4e00-\u9fa5]+"));
        System.out.println(",.!#$%@#$^*".matches("[\\W_]+"));
        System.out.println(Arrays.asList("sdfs,,  ,   ".split("(\\W*)")).toString());
        System.out.println(Arrays.asList("\"(sdf asffsdf ewgg？123???英文)问号?我是华丽[的制表符\t]我是华丽{的空格符 我是华丽}的换行符\n"
                .split("\\b")).toString());
    }

    @Test
    public void test2() {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        String str = " 3451sdgasdgas sdfasdfaw3t 2t23";
        Matcher slashMatcher = pattern.matcher(str);
        int idx = 0;
        int firstPos = 0;
        int endPos = 0;
        while (slashMatcher.find()) {
            if (idx == 1) {
                firstPos = slashMatcher.start();
            } else {
                endPos = slashMatcher.start();
            }
            idx++;
        }
        String firstString = str.substring(0, firstPos);
        String endString = str.substring(endPos, str.length());
        String resultSub = str.substring(firstPos, endPos);
        System.out.println("firstString  >>>>>  " + firstString);
        System.out.println("endString  >>>>>  " + endString);
        System.out.println("resultSub  >>>>>  " + resultSub);
    }

    @Test
    public void testPattern() {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]+");
        String str = "第三方哈克斯交党费哈,克斯京东方哈萨克剪,短发哈看时间点合法会计师电话费";
        Matcher slashMatcher = pattern.matcher(str);
        while (slashMatcher.find()) {
            System.out.println(slashMatcher.group());
            System.out.println(slashMatcher.start());
        }
    }

    @Test
    public void testWrite() throws IOException {
        File file = new File("E:\\metgefile.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        raf.write("345345345".getBytes());
        raf.seek(1024 * 1024);
        raf.write("sldkfjalsdkhflasdhfaklsjdhfas".getBytes());
        raf.close();
    }
}
