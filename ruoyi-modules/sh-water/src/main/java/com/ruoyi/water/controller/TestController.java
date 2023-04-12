package com.ruoyi.water.controller;

import com.ruoyi.common.core.domain.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author ：wt
 * @date ：Created in 2023-04-06 13:29
 * @description：
 * @modified By：wt
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/getByDay")
    public R getByDay() throws IOException {

        return R.ok("ok");
    }

}
