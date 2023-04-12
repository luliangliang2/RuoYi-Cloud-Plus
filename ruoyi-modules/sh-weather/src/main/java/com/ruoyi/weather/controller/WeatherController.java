package com.ruoyi.weather.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.weather.api.RemoteWeatherService;
import com.ruoyi.weather.common.WeatherForecastInfo;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 13:18
 * @description：
 * @modified By：wt
 */
@RestController
@RequestMapping("/info")
public class WeatherController {
    @Autowired
    RemoteWeatherService remoteWeatherService;

    @RequestMapping("/forecastByDay")
    public R getByDay(String cityCode) throws IOException {
        List<Map<String, Object>> maps = remoteWeatherService.acquireForecastByDay(cityCode);
        return R.ok(maps);
    }

    @RequestMapping("/forecastByHour")
    public R getByHour(String cityCode) throws IOException {
        List<WeatherForecastInfo> resp = remoteWeatherService.acquireForecastByHour(cityCode);
        return R.ok(resp);
    }
}
