package com.ruoyi.weather.api;


import com.ruoyi.weather.common.WeatherForecastInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 15:07
 * @description：
 * @modified By：wt
 */
public interface RemoteWeatherService {
    List<Map<String, Object>> acquireForecastByDay(String cityCode);
    List<WeatherForecastInfo> acquireForecastByHour(String cityCode) throws IOException;
}
