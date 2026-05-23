package org.dromara.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.manager.api.domain.BizVehicle;
import org.dromara.manager.constant.EquipmentTypeConstants;
import org.dromara.manager.domain.BizCamera;
import org.dromara.manager.domain.BizRadar;
import org.dromara.manager.domain.BizSimCard;
import org.dromara.manager.domain.BizVehicleEquipmentBind;
import org.dromara.manager.domain.bo.BizVehicleEquipmentBindBo;
import org.dromara.manager.domain.vo.BizVehicleEquipmentBindVo;
import org.dromara.manager.mapper.BizCameraMapper;
import org.dromara.manager.mapper.BizRadarMapper;
import org.dromara.manager.mapper.BizSimCardMapper;
import org.dromara.manager.mapper.BizVehicleEquipmentBindMapper;
import org.dromara.manager.mapper.BizVehicleMapper;
import org.dromara.manager.service.IBizVehicleEquipmentBindService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 车辆上装绑定Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizVehicleEquipmentBindServiceImpl implements IBizVehicleEquipmentBindService {

    private final BizVehicleEquipmentBindMapper baseMapper;
    private final BizVehicleMapper vehicleMapper;
    private final BizCameraMapper cameraMapper;
    private final BizRadarMapper radarMapper;
    private final BizSimCardMapper simCardMapper;

    /**
     * 查询车辆上装绑定
     *
     * @param bindId 主键
     * @return 车辆上装绑定
     */
    @Override
    public BizVehicleEquipmentBindVo queryById(Long bindId) {
        return baseMapper.selectVoById(bindId);
    }

    /**
     * 查询车辆上装绑定列表
     *
     * @param vehicleId 车辆ID
     * @param equipmentType 设备类型
     * @return 车辆上装绑定列表
     */
    @Override
    public List<BizVehicleEquipmentBindVo> queryList(Long vehicleId, String equipmentType) {
        return baseMapper.selectBindList(vehicleId, equipmentType);
    }

    /**
     * 新增车辆上装绑定
     *
     * @param bo 车辆上装绑定
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizVehicleEquipmentBindBo bo) {
        BizVehicleEquipmentBind add = MapstructUtils.convert(bo, BizVehicleEquipmentBind.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBindId(add.getBindId());
        }
        return flag;
    }

    /**
     * 修改车辆上装绑定
     *
     * @param bo 车辆上装绑定
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizVehicleEquipmentBindBo bo) {
        BizVehicleEquipmentBind update = MapstructUtils.convert(bo, BizVehicleEquipmentBind.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizVehicleEquipmentBind entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizVehicleEquipmentBind entity) {
        BizVehicle vehicle = vehicleMapper.selectById(entity.getVehicleId());
        if (vehicle == null) {
            throw new ServiceException("车辆不存在");
        }
        validEquipment(entity);
        Long bindCount = baseMapper.selectCount(new LambdaQueryWrapper<BizVehicleEquipmentBind>()
            .eq(BizVehicleEquipmentBind::getEquipmentType, entity.getEquipmentType())
            .eq(BizVehicleEquipmentBind::getEquipmentId, entity.getEquipmentId())
            .ne(entity.getBindId() != null, BizVehicleEquipmentBind::getBindId, entity.getBindId()));
        if (bindCount > 0) {
            throw new ServiceException("设备已绑定其他车辆，请先解绑");
        }
    }

    private void validEquipment(BizVehicleEquipmentBind entity) {
        if (EquipmentTypeConstants.CAMERA.equals(entity.getEquipmentType())) {
            BizCamera camera = cameraMapper.selectById(entity.getEquipmentId());
            if (camera == null) {
                throw new ServiceException("相机不存在");
            }
            if (!"0".equals(camera.getStatus())) {
                throw new ServiceException("相机已停用");
            }
            return;
        }
        if (EquipmentTypeConstants.RADAR.equals(entity.getEquipmentType())) {
            BizRadar radar = radarMapper.selectById(entity.getEquipmentId());
            if (radar == null) {
                throw new ServiceException("雷达不存在");
            }
            if (!"0".equals(radar.getStatus())) {
                throw new ServiceException("雷达已停用");
            }
            return;
        }
        if (EquipmentTypeConstants.SIM_CARD.equals(entity.getEquipmentType())) {
            BizSimCard simCard = simCardMapper.selectById(entity.getEquipmentId());
            if (simCard == null) {
                throw new ServiceException("SIM卡不存在");
            }
            if (!"0".equals(simCard.getStatus())) {
                throw new ServiceException("SIM卡已停用");
            }
            return;
        }
        throw new ServiceException("设备类型不正确");
    }

    /**
     * 校验并批量删除车辆上装绑定信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

}
