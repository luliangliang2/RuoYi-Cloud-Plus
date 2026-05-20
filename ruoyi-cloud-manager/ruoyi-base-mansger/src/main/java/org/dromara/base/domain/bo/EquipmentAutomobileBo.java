package org.dromara.base.domain.bo;

import org.dromara.base.domain.EquipmentAutomobile;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 车辆管理业务对象 equipment_automobile
 *
 * @author 路亮亮
 * @date 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = EquipmentAutomobile.class, reverseConvertGenerate = false)
public class EquipmentAutomobileBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
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
     * 车牌号
     */
    private String plateNumber;


}
