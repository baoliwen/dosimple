package com.dosimple.common.util;

import ch.qos.logback.core.util.CloseUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPUtils {
    private GZIPUtils() {
    }
    private static final String GZIP_ENCODE_UTF_8 = "UTF-8";
    private static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

    public static byte[] compress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            CloseUtil.closeQuietly(gzip);
            CloseUtil.closeQuietly(out);
        }
        return out.toByteArray();
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = null;
        try {
            ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[512];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            CloseUtil.closeQuietly(out);
            CloseUtil.closeQuietly(ungzip);
            CloseUtil.closeQuietly(in);
        }
        return out.toByteArray();
    }

    public static String compressToString(String str,String inEncoding){
        if (str == null || str.length() == 0) {
            return str;
        }
        String result;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(inEncoding));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            CloseUtil.closeQuietly(gzip);
        }
        //此处要等gzip关闭后再去转换
        try {
            result = out.toString(GZIP_ENCODE_ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }finally {
            CloseUtil.closeQuietly(out);
        }
        return result;
    }

    public static String uncompressToString(String str,String outEncoding){
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream gunzip = null;
        String result;
        try {
            in = new ByteArrayInputStream(str.getBytes(GZIP_ENCODE_ISO_8859_1));
            gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[512];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            result = out.toString(outEncoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtil.closeQuietly(gunzip);
            CloseUtil.closeQuietly(in);
            CloseUtil.closeQuietly(out);
        }
        return result;
    }

    public static String compressToString(String data) {
        return compressToString(data, GZIP_ENCODE_UTF_8);
    }

    public static String uncompressToString(String data) {
        return uncompressToString(data, GZIP_ENCODE_UTF_8);
    }

}
