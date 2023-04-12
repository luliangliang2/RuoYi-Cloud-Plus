package com.ruoyi.weather.dubbo;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.weather.api.RemoteWeatherService;
import com.ruoyi.weather.common.CityMapping;
import com.ruoyi.weather.common.WeatherForecastInfo;
import com.ruoyi.weather.mapper.CityMappingMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 13:37
 * @description：
 * @modified By：wt
 */
@Service
@DubboService
public class RemoteWeatherServiceImpl implements RemoteWeatherService {
    private final CityMappingMapper cityMappingMapper;

    @Autowired
    public RemoteWeatherServiceImpl(CityMappingMapper cityMappingMapper) {
        this.cityMappingMapper = cityMappingMapper;
    }

    @Override
    public List<Map<String, Object>> acquireForecastByDay(String cityCode) {
        String body = HttpRequest.get("http://www.nmc.cn/rest/weather?stationid=" + cityCode)
            .timeout(20000)//超时，毫秒
            .header("Host","www.nmc.cn")
            .header("Referer","http://www.nmc.cn/publish/forecast/ALN/shenyang.html")
            .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
            .execute().body();
        JSONArray jsonArray = JSONUtil.parseObj(body)
            .getJSONObject("data")
            .getJSONObject("predict")
            .getJSONArray("detail");

        List<Map<String, Object>> response = new ArrayList<>();
        for (Object o : jsonArray) {
            Map<String,Object> oneDayInfo = new HashMap<>();
            JSONObject obj = JSONUtil.parseObj(o);
            //日期
            String date = obj.get("date").toString();
            oneDayInfo.put("date",date);
            //白天
            WeatherForecastInfo dayInfo = new WeatherForecastInfo();
            JSONObject day = obj.getJSONObject("day");
            dayInfo.setDate(date);
            dayInfo.setInfo(day.getJSONObject("weather").getStr("info"));
            dayInfo.setTemp(day.getJSONObject("weather").getFloat("temperature"));
            dayInfo.setWindType(day.getJSONObject("wind").getStr("direct"));
            dayInfo.setWindPower(day.getJSONObject("wind").getStr("power"));
            //晚上
            WeatherForecastInfo nightInfo = new WeatherForecastInfo();
            JSONObject night = obj.getJSONObject("night");
            nightInfo.setDate(date);
            nightInfo.setInfo(night.getJSONObject("weather").getStr("info"));
            nightInfo.setTemp(night.getJSONObject("weather").getFloat("temperature"));
            nightInfo.setWindType(night.getJSONObject("wind").getStr("direct"));
            nightInfo.setWindPower(night.getJSONObject("wind").getStr("power"));

            oneDayInfo.put("day",dayInfo);
            oneDayInfo.put("night",nightInfo);
            response.add(oneDayInfo);
        }

        return response;
    }

    @Override
    public List<WeatherForecastInfo> acquireForecastByHour(String cityCode) throws IOException {
        CityMapping cityMapping = cityMappingMapper.selectOne(new QueryWrapper<CityMapping>().eq("weather_code", cityCode));

        String url = "http://www.nmc.cn/publish/forecast/" + cityMapping.getPathName() + "/" + cityMapping.getEnName() +".html";

        String body = HttpRequest.get(url)
            .timeout(20000)//超时，毫秒
            .header("Host","www.nmc.cn")
            .header("Referer","http://www.nmc.cn/publish/forecast/ALN/shenyang.html")
            .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
            .execute().body();

        Document doc = Jsoup.parse(body);
        Elements hour3s = doc.getElementsByClass("hour3");

        boolean flag = true;
        Date date = null;
        List<WeatherForecastInfo> list = new ArrayList<>();

        for (Element hour3 : hour3s) {


            Date forecastDateTime = null;
            if (flag){
                String hour = Objects.requireNonNull(Objects.requireNonNull(hour3.firstChild()).ownerDocument()).text();
                date = DateUtil.parseDate(DateUtil.today() + " " + hour + ":00");
                flag = false;
            }
            forecastDateTime = date;

            WeatherForecastInfo info = new WeatherForecastInfo();
            //时间
            info.setDate(DateUtil.formatDate(forecastDateTime));
            //温度
            String tempStr = hour3.child(3).text().replace("℃","");

            info.setTemp(Float.parseFloat(tempStr));
            //windType
            String windType = hour3.child(5).text();
            info.setWindType(windType);
            //windPower
            String windPower = hour3.child(4).text();
            info.setWindPower(windPower);
            list.add(info);
            date = DateUtil.offset(date, DateField.HOUR,3);
        }
        return list;
    }
}
