package com.ruoyi.weather.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 9:09
 * @description：
 * @modified By：wt
 */
@Data
public class WeatherSimpleInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cityName;
    private Float temp;
    private String windType;
    private String windPower;
    private String info;
}
