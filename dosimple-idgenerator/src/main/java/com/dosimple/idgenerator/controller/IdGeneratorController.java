package com.dosimple.idgenerator.controller;

import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.ResponseCode;
import com.dosimple.common.constant.UrlMappingConstant;
import com.dosimple.idgenerator.service.IIdGeneratorService;
import com.dosimple.rpc.client.IdGeneratorApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author baolw
 */
@RestController
@Slf4j
@RequestMapping(UrlMappingConstant.ID_GENERATOR)
public class IdGeneratorController implements IdGeneratorApiService {
    @Autowired
    private IIdGeneratorService iIdGeneratorService;
    @GetMapping("/generator")
    public ApiResult<List<Long>> generator(@RequestParam("bizTag") String bizTag) {
        List<Long> ids = iIdGeneratorService.generatorId(bizTag);
        if (null == ids) {
            return new ApiResult<>(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return new ApiResult<>(200, "", ids);
    }


}
