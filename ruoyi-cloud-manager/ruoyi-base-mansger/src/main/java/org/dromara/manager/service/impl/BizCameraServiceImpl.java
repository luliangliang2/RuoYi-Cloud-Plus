package org.dromara.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.constant.EquipmentTypeConstants;
import org.dromara.manager.domain.BizCamera;
import org.dromara.manager.domain.BizVehicleEquipmentBind;
import org.dromara.manager.domain.bo.BizCameraBo;
import org.dromara.manager.domain.vo.BizCameraVo;
import org.dromara.manager.mapper.BizCameraMapper;
import org.dromara.manager.mapper.BizVehicleEquipmentBindMapper;
import org.dromara.manager.service.IBizCameraService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 上装相机Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizCameraServiceImpl implements IBizCameraService {

    private final BizCameraMapper baseMapper;
    private final BizVehicleEquipmentBindMapper bindMapper;

    /**
     * 查询上装相机
     *
     * @param cameraId 主键
     * @return 上装相机
     */
    @Override
    public BizCameraVo queryById(Long cameraId) {
        return baseMapper.selectVoById(cameraId);
    }

    /**
     * 分页查询上装相机列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 上装相机分页列表
     */
    @Override
    public TableDataInfo<BizCameraVo> queryPageList(BizCameraBo bo, PageQuery pageQuery) {
        BizCamera query = MapstructUtils.convert(bo, BizCamera.class);
        Page<BizCameraVo> result = baseMapper.selectCameraPage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的上装相机列表
     *
     * @param bo 查询条件
     * @return 上装相机列表
     */
    @Override
    public List<BizCameraVo> queryList(BizCameraBo bo) {
        BizCamera query = MapstructUtils.convert(bo, BizCamera.class);
        return baseMapper.selectCameraList(query);
    }

    /**
     * 查询可绑定相机列表
     *
     * @param vehicleId 车辆ID
     * @param keyword 关键字
     * @return 上装相机列表
     */
    @Override
    public List<BizCameraVo> queryBindableList(Long vehicleId, String keyword) {
        return baseMapper.selectBindableList(vehicleId, keyword);
    }

    /**
     * 新增上装相机
     *
     * @param bo 上装相机
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizCameraBo bo) {
        BizCamera add = MapstructUtils.convert(bo, BizCamera.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setCameraId(add.getCameraId());
        }
        return flag;
    }

    /**
     * 修改上装相机
     *
     * @param bo 上装相机
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizCameraBo bo) {
        BizCamera update = MapstructUtils.convert(bo, BizCamera.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizCamera entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizCamera entity) {
        Long codeCount = baseMapper.selectCount(new LambdaQueryWrapper<BizCamera>()
            .eq(BizCamera::getCameraCode, entity.getCameraCode())
            .ne(entity.getCameraId() != null, BizCamera::getCameraId, entity.getCameraId()));
        if (codeCount > 0) {
            throw new ServiceException("相机编码已存在");
        }
        if (StringUtils.isNotBlank(entity.getSn())) {
            Long snCount = baseMapper.selectCount(new LambdaQueryWrapper<BizCamera>()
                .eq(BizCamera::getSn, entity.getSn())
                .ne(entity.getCameraId() != null, BizCamera::getCameraId, entity.getCameraId()));
            if (snCount > 0) {
                throw new ServiceException("设备SN号已存在");
            }
        }
    }

    /**
     * 校验并批量删除上装相机信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = bindMapper.selectCount(Wrappers.lambdaQuery(BizVehicleEquipmentBind.class)
                .eq(BizVehicleEquipmentBind::getEquipmentType, EquipmentTypeConstants.CAMERA)
                .in(BizVehicleEquipmentBind::getEquipmentId, ids));
            if (count > 0) {
                throw new ServiceException("相机已绑定车辆，不允许删除");
            }
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

}
