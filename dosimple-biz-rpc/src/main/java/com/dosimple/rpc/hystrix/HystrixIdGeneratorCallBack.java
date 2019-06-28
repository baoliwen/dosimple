package com.dosimple.rpc.hystrix;

import com.dosimple.common.constant.ApiResult;
import com.dosimple.rpc.BaseHystrixCallBack;
import com.dosimple.rpc.HystrixErrorResult;
import com.dosimple.rpc.client.IdGeneratorApiService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dosimple
 */
@Component
public class HystrixIdGeneratorCallBack extends BaseHystrixCallBack implements IdGeneratorApiService {

    @Override
    public ApiResult generator(String bizTag) {
        logging(bizTag);
        return HystrixErrorResult.getCallBackError();
    }
}
