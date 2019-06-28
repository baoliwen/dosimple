package com.dosimple.biz.user.controller;

import com.dosimple.common.constant.UrlMappingConstant;
import com.dosimple.biz.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试使用
 */
@RestController
public class UserIndexController {

    @Autowired
    private UserService userService;
    @GetMapping(UrlMappingConstant.SEARCH_USER_API+"/test/index")
    public String index() {
        return "hello search user";
    }
}
