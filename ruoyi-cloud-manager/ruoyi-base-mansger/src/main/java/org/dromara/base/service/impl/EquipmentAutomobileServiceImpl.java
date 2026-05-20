package org.dromara.base.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.base.domain.bo.EquipmentAutomobileBo;
import org.dromara.base.domain.vo.EquipmentAutomobileVo;
import org.dromara.base.domain.EquipmentAutomobile;
import org.dromara.base.mapper.EquipmentAutomobileMapper;
import org.dromara.base.service.IEquipmentAutomobileService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 车辆管理Service业务层处理
 *
 * @author 路亮亮
 * @date 2026-03-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EquipmentAutomobileServiceImpl implements IEquipmentAutomobileService {

    private final EquipmentAutomobileMapper baseMapper;

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    @Override
    public EquipmentAutomobileVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询车辆管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 车辆管理分页列表
     */
    @Override
    public TableDataInfo<EquipmentAutomobileVo> queryPageList(EquipmentAutomobileBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<EquipmentAutomobile> lqw = buildQueryWrapper(bo);
        Page<EquipmentAutomobileVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    @Override
    public List<EquipmentAutomobileVo> queryList(EquipmentAutomobileBo bo) {
        LambdaQueryWrapper<EquipmentAutomobile> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<EquipmentAutomobile> buildQueryWrapper(EquipmentAutomobileBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<EquipmentAutomobile> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(EquipmentAutomobile::getId);
        lqw.eq(StringUtils.isNotBlank(bo.getVin()), EquipmentAutomobile::getVin, bo.getVin());
        lqw.eq(StringUtils.isNotBlank(bo.getBrand()), EquipmentAutomobile::getBrand, bo.getBrand());
        lqw.eq(StringUtils.isNotBlank(bo.getPlateNumber()), EquipmentAutomobile::getPlateNumber, bo.getPlateNumber());
        return lqw;
    }

    /**
     * 新增车辆管理
     *
     * @param bo 车辆管理
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(EquipmentAutomobileBo bo) {
        EquipmentAutomobile add = MapstructUtils.convert(bo, EquipmentAutomobile.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改车辆管理
     *
     * @param bo 车辆管理
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(EquipmentAutomobileBo bo) {
        EquipmentAutomobile update = MapstructUtils.convert(bo, EquipmentAutomobile.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(EquipmentAutomobile entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除车辆管理信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
