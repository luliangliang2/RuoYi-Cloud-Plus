package com.ruoyi.jobs;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author ：wt
 * @date ：Created in 2023-04-10 11:16
 * @description：
 * @modified By：wt
 */
@Component
public class RealWeatherJobHandler{



    @XxlJob(value = "RealWeather")
    public void RealWeather(){
        System.out.println("weather");
    }
}
