package org.dromara.manager.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 告警查询业务对象
 *
 * @author LionLi
 * @date 2026-05-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizAlarmBo extends BaseEntity {

    /**
     * VIN码
     */
    private String vin;

    /**
     * 车牌
     */
    private String plateNo;

    /**
     * 车辆品牌
     */
    private String brand;

    /**
     * 告警编码
     */
    private Integer alarmCode;

    /**
     * 告警等级
     */
    private Integer alarmLevel;

    /**
     * 是否已处理 0未处理 1已处理
     */
    private Integer handled;

    /**
     * 告警开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 告警结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
