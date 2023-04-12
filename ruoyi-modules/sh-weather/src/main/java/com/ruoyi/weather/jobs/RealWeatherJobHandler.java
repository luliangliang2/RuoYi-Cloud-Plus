package com.ruoyi.weather.jobs;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.weather.common.CityMapping;
import com.ruoyi.weather.common.WeatherSimpleInfo;
import com.ruoyi.weather.mapper.CityMappingMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ruoyi.common.iotdb.utils.FormatUtils.formatResponse;

/**
 * @author ：wt
 * @date ：Created in 2023-04-10 11:16
 * @description：
 * @modified By：wt
 */
@Slf4j
@Component
public class RealWeatherJobHandler{

    private final CityMappingMapper cityMappingMapper;

    private final SessionPool sessionPool;

    ExecutorService pool = Executors.newSingleThreadExecutor();

    @Autowired
    public RealWeatherJobHandler(CityMappingMapper cityMappingMapper,
                                 SessionPool sessionPool) {
        this.cityMappingMapper = cityMappingMapper;
        this.sessionPool = sessionPool;
    }

    @XxlJob(value = "RealWeather")
    public void RealWeather() throws IoTDBConnectionException, StatementExecutionException {

        List<CityMapping> cities = cityMappingMapper.selectList(null);

        for (CityMapping city : cities) {
            Float realWeather = getRealWeather(city.getWeatherCode());
            WeatherSimpleInfo weatherInfo = getRealWeatherInfo(city.getWeatherCode());
            weatherInfo.setCityName(city.getCityName());
            weatherInfo.setTemp(realWeather);


            pool.execute(()->{
                log.info(DateUtil.now() + "当前室外天气状况:"+ weatherInfo);
                insertIntoIotDb("root.weather",city.getEnName(),new DateTime().getTime(),
                    Arrays.asList("temp","windType","windPower","info"),
                    Arrays.asList(weatherInfo.getTemp()==null?"0":weatherInfo.getTemp().toString(),
                        weatherInfo.getWindType(),
                        weatherInfo.getWindPower(),
                        weatherInfo.getInfo()));
                XxlJobHelper.log(DateUtil.now() + "当前室外天气状况:"+ weatherInfo);
            });
        }

    }

    /**
     * 向iotdb中插入数据
     * @param database = iotdb 中 storage group
     * @param table = iotdb 中 device
     * @param time = 数据采集时间
     * @param fields = 数据库中 字段
     * @param  values = 字段对应值
     */
    private void insertIntoIotDb(String database,String table,long time,List<String> fields,List<String> values){
        try {
            sessionPool.insertRecord(database + "." + table,time,
                fields,
                values);
        } catch (IoTDBConnectionException | StatementExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Float getRealWeather(String cityCode){
        String body = HttpRequest.get("http://www.nmc.cn/rest/weather?stationid=" + cityCode)
            .timeout(20000)//超时，毫秒
            .execute().body();
        return JSONUtil.parseObj(body)
            .getJSONObject("data")
            .getJSONObject("real")
            .getJSONObject("weather")
            .getFloat("temperature");
    }


    public WeatherSimpleInfo getRealWeatherInfo(String cityCode){
        String body = HttpRequest.get("http://www.nmc.cn/rest/weather?stationid=" + cityCode)
            .timeout(20000)//超时，毫秒
            .header("Host","www.nmc.cn")
            .header("Referer","http://www.nmc.cn/publish/forecast/ALN/shenyang.html")
            .execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(body)
            .getJSONObject("data")
            .getJSONObject("predict")
            .getJSONArray("detail")
            .getJSONObject(1)
            .getJSONObject("day");
        String dayInfo = jsonObject
            .getJSONObject("weather")
            .getStr("info");
        String windDirect = jsonObject
            .getJSONObject("wind")
            .getStr("direct");
        String windPower = jsonObject
            .getJSONObject("wind")
            .getStr("power");
        WeatherSimpleInfo weatherSimpleInfo = new WeatherSimpleInfo();
        weatherSimpleInfo.setInfo(dayInfo);
        weatherSimpleInfo.setWindType(windDirect);
        weatherSimpleInfo.setWindPower(windPower);
        return weatherSimpleInfo;
    }
}
