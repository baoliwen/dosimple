package com.dosimple.rpc;


import com.dosimple.common.constant.ApiResult;

public class HystrixErrorResult {

    /**
     * 查询失败
     * @return
     */
    public static ApiResult getCallBackError(){
        return new ApiResult(9001, "查询失败");
    }

    public static ApiResult postCallBackError(){
        return new ApiResult(9002, "提交失败");
    }

    public static ApiResult putCallBackError(){
        return new ApiResult(9003, "更新失败");
    }

    public static ApiResult deleteCallBackError(){
        return new ApiResult(9004, "删除失败");
    }
}
