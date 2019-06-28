package com.dosimple.biz.book.controller;

import com.dosimple.common.constant.UrlMappingConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: dosimple
 * @date: 2018/6/14 0014 下午 5:16
 */
@RestController
@Slf4j
public class BookIndexController {
    @GetMapping(UrlMappingConstant.SEARCH_BOOK_API+"/test/index")
    public String index() {
        return "hello,this is search-shop";
    }
}
