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
import org.dromara.manager.domain.BizRadar;
import org.dromara.manager.domain.BizVehicleEquipmentBind;
import org.dromara.manager.domain.bo.BizRadarBo;
import org.dromara.manager.domain.vo.BizRadarVo;
import org.dromara.manager.mapper.BizRadarMapper;
import org.dromara.manager.mapper.BizVehicleEquipmentBindMapper;
import org.dromara.manager.service.IBizRadarService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 上装雷达Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizRadarServiceImpl implements IBizRadarService {

    private final BizRadarMapper baseMapper;
    private final BizVehicleEquipmentBindMapper bindMapper;

    /**
     * 查询上装雷达
     *
     * @param radarId 主键
     * @return 上装雷达
     */
    @Override
    public BizRadarVo queryById(Long radarId) {
        return baseMapper.selectVoById(radarId);
    }

    /**
     * 分页查询上装雷达列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 上装雷达分页列表
     */
    @Override
    public TableDataInfo<BizRadarVo> queryPageList(BizRadarBo bo, PageQuery pageQuery) {
        BizRadar query = MapstructUtils.convert(bo, BizRadar.class);
        Page<BizRadarVo> result = baseMapper.selectRadarPage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的上装雷达列表
     *
     * @param bo 查询条件
     * @return 上装雷达列表
     */
    @Override
    public List<BizRadarVo> queryList(BizRadarBo bo) {
        BizRadar query = MapstructUtils.convert(bo, BizRadar.class);
        return baseMapper.selectRadarList(query);
    }

    /**
     * 查询可绑定雷达列表
     *
     * @param vehicleId 车辆ID
     * @param keyword 关键字
     * @return 上装雷达列表
     */
    @Override
    public List<BizRadarVo> queryBindableList(Long vehicleId, String keyword) {
        return baseMapper.selectBindableList(vehicleId, keyword);
    }

    /**
     * 新增上装雷达
     *
     * @param bo 上装雷达
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizRadarBo bo) {
        BizRadar add = MapstructUtils.convert(bo, BizRadar.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRadarId(add.getRadarId());
        }
        return flag;
    }

    /**
     * 修改上装雷达
     *
     * @param bo 上装雷达
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizRadarBo bo) {
        BizRadar update = MapstructUtils.convert(bo, BizRadar.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizRadar entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizRadar entity) {
        Long codeCount = baseMapper.selectCount(new LambdaQueryWrapper<BizRadar>()
            .eq(BizRadar::getRadarCode, entity.getRadarCode())
            .ne(entity.getRadarId() != null, BizRadar::getRadarId, entity.getRadarId()));
        if (codeCount > 0) {
            throw new ServiceException("雷达编码已存在");
        }
        if (StringUtils.isNotBlank(entity.getSn())) {
            Long snCount = baseMapper.selectCount(new LambdaQueryWrapper<BizRadar>()
                .eq(BizRadar::getSn, entity.getSn())
                .ne(entity.getRadarId() != null, BizRadar::getRadarId, entity.getRadarId()));
            if (snCount > 0) {
                throw new ServiceException("设备SN号已存在");
            }
        }
    }

    /**
     * 校验并批量删除上装雷达信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = bindMapper.selectCount(Wrappers.lambdaQuery(BizVehicleEquipmentBind.class)
                .eq(BizVehicleEquipmentBind::getEquipmentType, EquipmentTypeConstants.RADAR)
                .in(BizVehicleEquipmentBind::getEquipmentId, ids));
            if (count > 0) {
                throw new ServiceException("雷达已绑定车辆，不允许删除");
            }
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

}
