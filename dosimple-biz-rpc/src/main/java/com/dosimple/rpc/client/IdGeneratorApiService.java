package com.dosimple.rpc.client;

import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.ModuleServiceConstant;
import com.dosimple.common.constant.UrlMappingConstant;
import com.dosimple.rpc.hystrix.HystrixIdGeneratorCallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author baolw
 */
@Service
@FeignClient(value = ModuleServiceConstant.ID_GENERATOR, fallback = HystrixIdGeneratorCallBack.class)
public interface IdGeneratorApiService {

    @GetMapping(UrlMappingConstant.ID_GENERATOR+"/generator")
    ApiResult<List<Long>> generator(@RequestParam("bizTag") String bizTag);

}
