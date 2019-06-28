package com.dosimple.common.constant;

public enum RedisKeys {
    PAY_ORDER_ID_GENERATOR("PAY_ORDER_ID_GENERATOR"),
    LOCK_ID_GENERATOR("LOCK_ID_GENERATOR");


    RedisKeys(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;

    public String getPrefix() {
        return prefix;
    }


    public String build(Object... args) {
        StringBuilder sb = new StringBuilder(prefix);
        for (Object arg : args) {
            sb.append(":").append(arg);
        }
        return sb.toString();
    }

    public static RedisKeys transform(String key) {
        for (RedisKeys redisKeys : RedisKeys.values()) {
            if (redisKeys.name().equals(key)) {
                return redisKeys;
            }
        }
        return null;
    }
}