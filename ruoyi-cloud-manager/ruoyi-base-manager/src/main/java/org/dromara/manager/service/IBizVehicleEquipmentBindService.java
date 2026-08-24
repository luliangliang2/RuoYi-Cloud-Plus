package org.dromara.manager.service;

import org.dromara.manager.domain.bo.BizVehicleEquipmentBindBo;
import org.dromara.manager.domain.vo.BizVehicleEquipmentBindVo;

import java.util.Collection;
import java.util.List;

/**
 * 车辆上装绑定Service接口
 *
 * @author LionLi
 * @date 2026-05-22
 */
public interface IBizVehicleEquipmentBindService {

    /**
     * 查询车辆上装绑定
     *
     * @param bindId 主键
     * @return 车辆上装绑定
     */
    BizVehicleEquipmentBindVo queryById(Long bindId);

    /**
     * 查询车辆上装绑定列表
     *
     * @param vehicleId 车辆ID
     * @param equipmentType 设备类型
     * @return 车辆上装绑定列表
     */
    List<BizVehicleEquipmentBindVo> queryList(Long vehicleId, String equipmentType);

    /**
     * 新增车辆上装绑定
     *
     * @param bo 车辆上装绑定
     * @return 是否新增成功
     */
    Boolean insertByBo(BizVehicleEquipmentBindBo bo);

    /**
     * 修改车辆上装绑定
     *
     * @param bo 车辆上装绑定
     * @return 是否修改成功
     */
    Boolean updateByBo(BizVehicleEquipmentBindBo bo);

    /**
     * 校验并批量删除车辆上装绑定信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
