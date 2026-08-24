package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizVehicleEquipmentBind;
import org.dromara.manager.domain.vo.BizVehicleEquipmentBindVo;

import java.util.List;

/**
 * 车辆上装绑定Mapper接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface BizVehicleEquipmentBindMapper extends BaseMapperPlus<BizVehicleEquipmentBind, BizVehicleEquipmentBindVo> {

    /**
     * 查询车辆上装绑定列表
     *
     * @param vehicleId 车辆ID
     * @param equipmentType 设备类型
     * @return 车辆上装绑定列表
     */
    List<BizVehicleEquipmentBindVo> selectBindList(@Param("vehicleId") Long vehicleId,
                                                   @Param("equipmentType") String equipmentType);

}
