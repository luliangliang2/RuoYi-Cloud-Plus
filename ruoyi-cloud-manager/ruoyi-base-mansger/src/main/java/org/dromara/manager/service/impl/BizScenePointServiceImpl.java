package org.dromara.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizScenePoint;
import org.dromara.manager.domain.BizSceneRoute;
import org.dromara.manager.domain.bo.BizScenePointBo;
import org.dromara.manager.domain.vo.BizScenePointVo;
import org.dromara.manager.mapper.BizScenePointMapper;
import org.dromara.manager.mapper.BizSceneRouteMapper;
import org.dromara.manager.service.IBizScenePointService;
import org.dromara.manager.service.support.TreeCategoryBindSupport;
import org.dromara.manager.utils.CoordinateConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 场景点位Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizScenePointServiceImpl implements IBizScenePointService {

    private static final String BUSINESS_TYPE = "scenePoint";

    private final BizScenePointMapper baseMapper;
    private final BizSceneRouteMapper sceneRouteMapper;
    private final TreeCategoryBindSupport categoryBindSupport;

    /**
     * 查询场景点位
     *
     * @param pointId 主键
     * @return 场景点位
     */
    @Override
    public BizScenePointVo queryById(Long pointId) {
        BizScenePointVo vo = baseMapper.selectVoById(pointId);
        fillCategoryNodeIds(vo);
        return vo;
    }

    /**
     * 分页查询场景点位列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 场景点位分页列表
     */
    @Override
    public TableDataInfo<BizScenePointVo> queryPageList(BizScenePointBo bo, PageQuery pageQuery) {
        BizScenePoint query = MapstructUtils.convert(bo, BizScenePoint.class);
        Page<BizScenePointVo> result = baseMapper.selectScenePointPage(pageQuery.build(), query);
        result.getRecords().forEach(this::fillCategoryNodeIds);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的场景点位列表
     *
     * @param bo 查询条件
     * @return 场景点位列表
     */
    @Override
    public List<BizScenePointVo> queryList(BizScenePointBo bo) {
        BizScenePoint query = MapstructUtils.convert(bo, BizScenePoint.class);
        List<BizScenePointVo> list = baseMapper.selectScenePointList(query);
        list.forEach(this::fillCategoryNodeIds);
        return list;
    }

    /**
     * 新增场景点位
     *
     * @param bo 场景点位
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(BizScenePointBo bo) {
        normalizeCategory(bo);
        BizScenePoint add = MapstructUtils.convert(bo, BizScenePoint.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPointId(add.getPointId());
            categoryBindSupport.save(BUSINESS_TYPE, bo.getPointId(), bo.getTreeId(), bo.getCategoryNodeIds());
        }
        return flag;
    }

    /**
     * 修改场景点位
     *
     * @param bo 场景点位
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(BizScenePointBo bo) {
        normalizeCategory(bo);
        BizScenePoint update = MapstructUtils.convert(bo, BizScenePoint.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            categoryBindSupport.save(BUSINESS_TYPE, bo.getPointId(), bo.getTreeId(), bo.getCategoryNodeIds());
        }
        return flag;
    }

    private void fillDefaultValue(BizScenePoint entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizScenePoint entity) {
        BizSceneRoute route = sceneRouteMapper.selectById(entity.getRouteId());
        if (route == null) {
            throw new ServiceException("路线不存在");
        }
        if (entity.getTreeId() == null) {
            entity.setTreeId(route.getTreeId());
        }
        if (entity.getCategoryNodeId() == null) {
            entity.setCategoryNodeId(route.getCategoryNodeId());
        }
        if (!entity.getTreeId().equals(route.getTreeId())
            || !entity.getCategoryNodeId().equals(route.getCategoryNodeId())) {
            throw new ServiceException("点位分类与路线分类不一致");
        }

        CoordinateConvertUtils.Coordinate bd09 = CoordinateConvertUtils.convertPoint(
            entity.getGcj02Lng().doubleValue(),
            entity.getGcj02Lat().doubleValue(),
            point -> CoordinateConvertUtils.gcj02ToBd09(point.lng(), point.lat())
        );
        CoordinateConvertUtils.Coordinate wgs84 = CoordinateConvertUtils.convertPoint(
            entity.getGcj02Lng().doubleValue(),
            entity.getGcj02Lat().doubleValue(),
            point -> CoordinateConvertUtils.gcj02ToWgs84(point.lng(), point.lat())
        );
        entity.setBd09Lng(BigDecimal.valueOf(bd09.lng()));
        entity.setBd09Lat(BigDecimal.valueOf(bd09.lat()));
        entity.setWgs84Lng(BigDecimal.valueOf(wgs84.lng()));
        entity.setWgs84Lat(BigDecimal.valueOf(wgs84.lat()));

        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizScenePoint>()
            .eq(BizScenePoint::getPointName, entity.getPointName())
            .ne(entity.getPointId() != null, BizScenePoint::getPointId, entity.getPointId()));
        if (count > 0) {
            throw new ServiceException("点位名称已存在");
        }
    }

    /**
     * 校验并批量删除场景点位信息
     *
     * @param ids 待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        categoryBindSupport.deleteByBusinessIds(BUSINESS_TYPE, ids);
        return baseMapper.deleteByIds(ids) > 0;
    }

    private void normalizeCategory(BizScenePointBo bo) {
        List<Long> nodeIds = categoryBindSupport.normalize(bo.getCategoryNodeId(), bo.getCategoryNodeIds());
        bo.setCategoryNodeIds(nodeIds);
        bo.setCategoryNodeId(nodeIds.isEmpty() ? null : nodeIds.get(0));
    }

    private void fillCategoryNodeIds(BizScenePointVo vo) {
        if (vo == null || vo.getPointId() == null) {
            return;
        }
        vo.setCategoryNodeIds(categoryBindSupport.getNodeIds(BUSINESS_TYPE, vo.getPointId(), vo.getCategoryNodeId()));
    }

}
