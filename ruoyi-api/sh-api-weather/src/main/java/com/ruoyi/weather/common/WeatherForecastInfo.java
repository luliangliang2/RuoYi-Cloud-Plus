package com.ruoyi.weather.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 15:20
 * @description：
 * @modified By：wt
 */
@Data
public class WeatherForecastInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String date;
    private Float Temp;
    private String WindType;
    private String WindPower;
    private String info;
}
