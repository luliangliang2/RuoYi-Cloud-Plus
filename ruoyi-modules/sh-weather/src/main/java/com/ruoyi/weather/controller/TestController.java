package com.ruoyi.weather.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：wt
 * @date ：Created in 2023-04-06 13:29
 * @description：
 * @modified By：wt
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/get")
    public String get(){
        return "weather";
    }
}
