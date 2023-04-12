package com.ruoyi.weather.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.weather.api.RemoteWeatherService;
import com.ruoyi.weather.common.WeatherForecastInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：wt
 * @date ：Created in 2023-04-06 13:29
 * @description：
 * @modified By：wt
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    RemoteWeatherService remoteWeatherService;

    @RequestMapping("/getByDay")
    public R getByDay() throws IOException {
        remoteWeatherService.acquireForecastByHour("54342");
        List<Map<String, Object>> maps = remoteWeatherService.acquireForecastByDay("54342");
        return R.ok(maps);
    }

    @RequestMapping("/getByHour")
    public R getByHour() throws IOException {
        List<WeatherForecastInfo> resp = remoteWeatherService.acquireForecastByHour("54342");
        return R.ok(resp);
    }
}
