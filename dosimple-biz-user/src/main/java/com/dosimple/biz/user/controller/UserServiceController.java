package com.dosimple.biz.user.controller;

import com.dosimple.biz.user.service.UserService;
import com.dosimple.rpc.client.UserServiceApiService;
import com.dosimple.common.constant.ApiResult;
import com.dosimple.common.constant.UrlMappingConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dosimple
 */
@Slf4j
@RestController
@RequestMapping(UrlMappingConstant.SEARCH_USER_API)
public class UserServiceController implements UserServiceApiService {
    @Autowired
    private UserService userService;
    /**
     * RPC调用DEMO,睡眠4s是测试hystrix的连接超时时间设置的6s是否有效，默认是1s的
     * @param demo
     * @return
     */
    @Override
    @GetMapping("/demo")
    public ApiResult firstDemo(@RequestParam("demo") String demo) {
        /*try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        userService.insertDb1();
        log.info("demo success >>>>>>>>>>>>>>>> {}", demo);
        return new ApiResult(200, "success");
    }

    @Override
    @GetMapping("/txTranstion")
    public ApiResult txTranstion(String demo) {
        userService.insertDb3();
        log.info("demo success >>>>>>>>>>>>>>>> {}", demo);
        return new ApiResult(200, "success");
    }

}



