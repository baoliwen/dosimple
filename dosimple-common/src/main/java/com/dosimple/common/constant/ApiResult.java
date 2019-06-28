package com.dosimple.common.constant;

import lombok.Data;

@Data
public class ApiResult<T> {

    private int code;
    private String msg;
    private T data;

    public ApiResult() {
    }

    public ApiResult(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public ApiResult(int code, String message) {
        this.code = code;
        this.msg = message;
    }
    public ApiResult(ResponseCode response) {
        this.code = response.getCode();
        this.msg = response.getMessage();
    }

    public boolean success() {
        return this.code == 200;
    }
    public boolean fail() {
        return !success();
    }

}
