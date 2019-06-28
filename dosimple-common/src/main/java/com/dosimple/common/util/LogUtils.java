package com.dosimple.common.util;

import ch.qos.logback.classic.Level;
import com.dosimple.common.io.SplitFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author dosimple
 */

public class LogUtils {
    private static final Logger logger = LoggerFactory.getLogger(SplitFile.class.getName());
    /**
     * @param log
     * @param message
     * @param dto
     */
    public static void logInfo(Logger log, String message, Object dto) {
        logObj(log, Level.INFO, message, dto);
    }
    public static void logError(Logger log, String message, Object dto) {
        logObj(log, Level.ERROR, message, dto);
    }

    public static void logWarn(Logger log, String message, Object dto) {
        logObj(log, Level.WARN, message, dto);
    }
    /** 示例：LogUtils.error(log, "你好这是一个示例.{data}", "示例")
     * @param log
     * @param pattern 打印的内容
     * @param params  参数
     */
    public static void error(Logger log, String pattern, Object... params) {
        log(log, Level.ERROR, pattern, params);
    }

    public static void warn(Logger log, String pattern, Object... params) {
        log(log, Level.WARN, pattern, params);
    }

    public static void info(Logger log, String pattern, Object... params) {
        log(log, Level.INFO, pattern, params);
    }

    private static void logObj(Logger log, Level level, String message, Object obj) {
        if (null == message) {
            return;
        }
        StringBuffer sb = new StringBuffer("\n"+message);
        try {
            Class<?> cls = obj.getClass();
            Map<String, String> map = new LinkedHashMap<>();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                Method rM = pd.getReadMethod();
                Object num = rM.invoke(obj);
                map.put(field.getName(), num == null ? null : num.toString());
            }
            print(log, level, null, sb, map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印日志失败。日志打印内容={}, 日志打印内容中的参数={}", sb, GsonHelper.toJson(obj));
        }
    }
    /**
     * @param log
     * @param pattern 打印的内容
     * @param params  参数
     */
    private static void log(Logger log, Level level, String pattern, Object... params) {
        if (null == pattern) {
            return;
        }
        List<String> keys = new ArrayList<>(10);
        StringBuffer message = new StringBuffer();

        int partKey = 0;
        int partDesc = 0;
        boolean isKeyChar = false;
        boolean firstDesc = false;

        StringBuilder[] segments = new StringBuilder[pattern.length()/2+1];
        StringBuilder[] descs = new StringBuilder[pattern.length()/2+1];
        try {
            for (int i = 0; i < pattern.length(); i++) {
                char ch = pattern.charAt(i);
                if (i == 0 && ch != '{') {
                    firstDesc = true;
                }
                if (!isKeyChar && ch != '{') {
                    if (firstDesc) {
                        if (descs[partDesc] == null) {
                            descs[partDesc] = new StringBuilder();

                        }
                        descs[partDesc].append(ch);
                        continue;
                    }
                    if (descs[partDesc - 1] == null) {
                        descs[partDesc - 1] = new StringBuilder();

                    }
                    descs[partDesc - 1].append(ch);
                }
                if (ch == '{') {
                    if (segments[partKey] == null) {
                        segments[partKey] = new StringBuilder();
                    }
                    partDesc++;
                    isKeyChar = true;
                    continue;
                } else if (ch == '}') {
                    if (null != segments[partKey]) {
                        keys.add(segments[partKey].toString());
                    }
                    partKey++;
                    isKeyChar = false;
                }
                if (isKeyChar) {
                    segments[partKey].append(ch);
                }
            }
            Map<String, String> map = new LinkedHashMap<>();

            for (int i = 0; i < keys.size(); i++) {
                if (firstDesc) {
                    if (descs[i] != null) {
                        map.put("desc" + i, descs[i].toString());
                    }
                    String value;
                    if (null == params) {
                        value = "null";
                    } else {
                        value = i >= params.length ? null : params[i] == null ? "null" : params[i].toString();
                    }
                    map = saveValue(map, keys.get(i), value, i);
                } else {
                    String value;
                    if (null == params) {
                        value = "null";
                    } else {
                        value = i >= params.length ? null : params[i] == null ? "null" : params[i].toString();
                    }
                    map = saveValue(map, keys.get(i), value, i);

                    if (descs[i] != null) {
                        map.put("desc" + i, descs[i].toString());
                    }
                }

            }
            print(log, level, pattern, message, map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印日志失败。日志打印内容={}, 日志打印内容中的参数={}", pattern, Arrays.toString(params));
        }
    }

    private static void print(Logger log, Level level, String pattern, StringBuffer message, Map<String, String> map) {
        if (map.isEmpty()) {
            message.append("\n").append(pattern);
        } else {
            map.forEach((key, value) -> {
                message.append("\n");
                if (!key.contains("desc")) {
                    message.append("[").append(key).append("]").append("\t\t\t\t\t\t\t\t\t\t");
                }
                message.append(value == null ? "[null]" : "["+value+"]");
            });
        }
        message.insert(0, "\n=========================================================");
        message.append("\n=========================================================");
        switch (level.levelInt) {
            case -2147483648:
                log.info(message.toString());
                break;
            case 5000:
                log.trace(message.toString());
                break;
            case 10000:
                log.debug(message.toString());
                break;
            case 20000:
                log.info(message.toString());
                break;
            case 30000:
                log.warn(message.toString());
                break;
            case 40000:
                log.error(message.toString());
                break;
            case 2147483647:
                log.info(message.toString());
                break;
            default:
                log.info(message.toString());
        }
    }

    private static Map<String, String> saveValue(Map<String, String> map, String key, String value, int i) {
        if (StringUtils.isBlank(key)) {
            key = "参数";
        }
        if (map.containsKey(key)) {
            map.put(key + i, value);
        } else {
            map.put(key, value);
        }
        return map;
    }
}
