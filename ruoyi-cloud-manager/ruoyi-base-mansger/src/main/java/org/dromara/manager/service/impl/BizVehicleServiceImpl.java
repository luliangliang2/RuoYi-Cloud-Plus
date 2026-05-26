package org.dromara.manager.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizTreeCategoryBind;
import org.dromara.manager.mapper.BizVehicleMapper;
import org.dromara.manager.mapper.BizTreeCategoryBindMapper;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import org.dromara.manager.api.IBizVehicleService;
import org.dromara.manager.api.domain.BizVehicle;
import org.dromara.manager.api.domain.bo.BizVehicleBo;
import org.dromara.manager.api.domain.vo.BizVehicleVo;
/**
 * 车辆管理Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-21
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizVehicleServiceImpl implements IBizVehicleService {

    private static final String BUSINESS_TYPE = "vehicle";

    private final BizVehicleMapper baseMapper;
    private final BizTreeCategoryBindMapper categoryBindMapper;

    /**
     * 查询车辆管理
     *
     * @param id 主键
     * @return 车辆管理
     */
    @Override
    public BizVehicleVo queryById(Long id){
        BizVehicleVo vo = baseMapper.selectVehicleVoById(id);
        fillCategoryNodeIds(vo);
        return vo;
    }

    /**
     * 分页查询车辆管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 车辆管理分页列表
     */
    @Override
    public TableDataInfo<BizVehicleVo> queryPageList(BizVehicleBo bo, PageQuery pageQuery) {
        Page<BizVehicleVo> result = baseMapper.selectVehiclePage(pageQuery.build(), bo);
        result.getRecords().forEach(this::fillCategoryNodeIds);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的车辆管理列表
     *
     * @param bo 查询条件
     * @return 车辆管理列表
     */
    @Override
    public List<BizVehicleVo> queryList(BizVehicleBo bo) {
        List<BizVehicleVo> list = baseMapper.selectVehicleList(bo);
        list.forEach(this::fillCategoryNodeIds);
        return list;
    }

    /**
     * 新增车辆管理
     *
     * @param bo 车辆管理
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(BizVehicleBo bo) {
        normalizeCategory(bo);
        BizVehicle add = MapstructUtils.convert(bo, BizVehicle.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            saveCategoryBinds(bo.getId(), bo.getTreeId(), bo.getCategoryNodeIds());
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(BizVehicleBo bo) {
        normalizeCategory(bo);
        BizVehicle update = MapstructUtils.convert(bo, BizVehicle.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            saveCategoryBinds(bo.getId(), bo.getTreeId(), bo.getCategoryNodeIds());
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizVehicle entity){
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        categoryBindMapper.deleteByBusinessIds(BUSINESS_TYPE, ids);
        return baseMapper.deleteByIds(ids) > 0;
    }

    private void normalizeCategory(BizVehicleBo bo) {
        List<Long> nodeIds = new ArrayList<>();
        if (bo.getCategoryNodeIds() != null) {
            nodeIds.addAll(bo.getCategoryNodeIds().stream().filter(java.util.Objects::nonNull).distinct().toList());
        } else if (bo.getCategoryNodeId() != null) {
            nodeIds.add(bo.getCategoryNodeId());
        }
        bo.setCategoryNodeIds(nodeIds);
        bo.setCategoryNodeId(nodeIds.isEmpty() ? null : nodeIds.get(0));
    }

    private void saveCategoryBinds(Long businessId, Long treeId, List<Long> nodeIds) {
        categoryBindMapper.deleteByBusiness(BUSINESS_TYPE, businessId);
        if (nodeIds == null || nodeIds.isEmpty()) {
            return;
        }
        for (Long nodeId : nodeIds) {
            BizTreeCategoryBind bind = new BizTreeCategoryBind();
            bind.setBusinessType(BUSINESS_TYPE);
            bind.setBusinessId(businessId);
            bind.setTreeId(treeId);
            bind.setNodeId(nodeId);
            categoryBindMapper.insert(bind);
        }
    }

    private void fillCategoryNodeIds(BizVehicleVo vo) {
        if (vo == null || vo.getId() == null) {
            return;
        }
        List<Long> nodeIds = categoryBindMapper.selectNodeIds(BUSINESS_TYPE, vo.getId());
        if (nodeIds.isEmpty() && vo.getCategoryNodeId() != null) {
            nodeIds = List.of(vo.getCategoryNodeId());
        }
        vo.setCategoryNodeIds(nodeIds);
    }
}
