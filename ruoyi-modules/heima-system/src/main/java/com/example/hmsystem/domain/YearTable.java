package com.example.hmsystem.domain;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Classname YearTable
 * @Author: ZhiYu Ren
 * @Date: 2023年04月07日 15:20
 * @Description:   年度表实体
 */
@Data
@TableName("sys_yeartable")
public class YearTable {
    // 年度id
    @TableId("year_id")
    private String yearId;
    //房间ID
    @TableField("dept_id")
    private Long deptId;
    //年度名称
    @TableField("year_name")
    private String yearName;
    //年度开始日期
    @TableField("year_start_day")
    private String yearStartDay;
    //年度结束日期
    @TableField("year_end_day")
    private String yearEndDay;
    //供暖开始日期
    @TableField("heating_start_day")
    private String heatingStartDay;
    //供暖结束日期
    @TableField("heating_end_day")
    private String heatingEndDay;
    //备注
    @TableField("remark")
    private String remark;
    //创建时间
    @TableField("create_time")
    private String createTime;
}
