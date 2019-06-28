package com.dosimple.common.constant;

/**
 * @author baolw
 */
public enum ResponseCode {
    DISABLE_USER(5001, "用户已进入黑名单"),
    INTERNAL_SERVER_ERROR(500, "服务器错误");
    public int code;
    public String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseCode valueOf(int code) {
        for (ResponseCode errorCode :  ResponseCode.values()) {
            if (code == errorCode.code) {
                return errorCode;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
