package com.dosimple.rpc.client;

import com.dosimple.rpc.hystrix.HystrixUserServiceApiServiceCallBack;
import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.ModuleServiceConstant;
import com.dosimple.common.constant.UrlMappingConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author dosimple
 */
@Service
@FeignClient(value = ModuleServiceConstant.SEARCH_USER, fallback = HystrixUserServiceApiServiceCallBack.class)
public interface UserServiceApiService {

    @GetMapping(UrlMappingConstant.SEARCH_USER_API+"/demo")
    ApiResult firstDemo(@RequestParam("demo") String demo);

    @GetMapping(UrlMappingConstant.SEARCH_USER_API+"/txTranstion")
    ApiResult txTranstion(@RequestParam("demo") String demo);

}
