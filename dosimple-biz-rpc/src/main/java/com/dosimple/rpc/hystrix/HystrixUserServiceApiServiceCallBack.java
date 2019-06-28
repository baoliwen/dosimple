package com.dosimple.rpc.hystrix;

import com.dosimple.rpc.BaseHystrixCallBack;
import com.dosimple.rpc.HystrixErrorResult;
import com.dosimple.rpc.client.UserServiceApiService;
import com.dosimple.common.constant.ApiResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;

/**
 * @author dosimple
 */
@Component
public class HystrixUserServiceApiServiceCallBack extends BaseHystrixCallBack implements UserServiceApiService {

    @Override
    public ApiResult firstDemo(String demo) {
        logging(demo);
        return HystrixErrorResult.getCallBackError();
    }

    @Override
    public ApiResult txTranstion(String demo) {
        logging(demo);
        return HystrixErrorResult.getCallBackError();
    }
}
