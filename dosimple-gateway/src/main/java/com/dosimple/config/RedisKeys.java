package com.dosimple.config;

/**
 * redis  key
 *
 */
public enum RedisKeys {
    BLACK_IP_LIST("IP黑名单");

    private String value;

    RedisKeys(String value) {
        this.value = value;
    }

    public String getPrefix() {
        return this.name();
    }

    public String build(Object... args) {
        StringBuilder sb = new StringBuilder(this.name());
        for (Object arg : args) {
            sb.append(":").append(arg);
        }
        return sb.toString();
    }

}
