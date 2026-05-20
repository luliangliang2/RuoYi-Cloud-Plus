package org.dromara.base.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 车辆管理对象 equipment_automobile
 *
 * @author 路亮亮
 * @date 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("equipment_automobile")
public class EquipmentAutomobile extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * vin码
     */
    private String vin;

    /**
     * 车辆型号
     */
    private String brand;

    /**
     * 删除标志
     */
    @TableLogic
    private Long delFlag;

    /**
     * 车牌号
     */
    private String plateNumber;


}
