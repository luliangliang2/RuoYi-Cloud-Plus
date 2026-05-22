package org.dromara.manager.api.domain.bo;

import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.api.domain.BizVehicle;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 车辆管理业务对象 biz_vehicle
 *
 * @author LionLi
 * @date 2026-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizVehicle.class, reverseConvertGenerate = false)
public class BizVehicleBo extends BaseEntity {

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
     * 车牌
     */
    private String plateNo;

    /**
     * 车辆品牌
     */
    private String brand;


}
