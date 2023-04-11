package com.ruoyi.weather.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 8:13
 * @description：
 * @modified By：wt
 */
@Data
@TableName("city_mapping")
public class CityMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("city_name")
    private String cityName;
    @TableField("en_name")
    private String enName;
    @TableField("weather_code")
    private String weatherCode;
    @TableField("hf_code")
    private String hfCode;
}
