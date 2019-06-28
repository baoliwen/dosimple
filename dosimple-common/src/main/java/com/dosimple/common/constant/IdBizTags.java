package com.dosimple.common.constant;

/**
 * @author baolw
 */
public enum IdBizTags {
    PAY_ORDER("支付订单", "PAY_ORDER_ID_GENERATOR");

    private String message;
    private String redisKey;

    IdBizTags(String message, String redisKey) {
        this.message = message;
        this.redisKey = redisKey;
    }

    public String getMessage() {
        return message;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public static String transformToRedisKey(String bizTag) {
        for (IdBizTags idBizTags : IdBizTags.values()) {
            if (idBizTags.name().equals(bizTag)) {
                return idBizTags.getRedisKey();
            }
        }
        return null;
    }
}
