package com.dosimple.biz.shop.controller;

import com.dosimple.biz.shop.service.OrderInfoService;
import com.dosimple.common.constant.UrlMappingConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: dosimple
 * @date: 2018/6/14 0014 下午 5:00
 */
@RestController
@Slf4j
@RequestMapping(UrlMappingConstant.SEARCH_SHOP_API)
public class ShopIndexController {
    @GetMapping("/test/index")
    public String index() {
        return "hello,this is search-shop";
    }

    @Autowired
    private OrderInfoService orderInfoService;
    @GetMapping("/test/tx")
    public String testTx() {
        long old = System.currentTimeMillis();
        orderInfoService.callRemoteRpc();
        log.info("time use >>>> {}", System.currentTimeMillis()-old);
        return "success";
    }
}
