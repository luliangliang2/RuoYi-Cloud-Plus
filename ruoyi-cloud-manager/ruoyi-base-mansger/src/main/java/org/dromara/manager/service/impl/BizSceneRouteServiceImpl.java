package org.dromara.manager.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.BizSceneRoute;
import org.dromara.manager.domain.bo.BizSceneRouteBo;
import org.dromara.manager.domain.vo.BizSceneRouteVo;
import org.dromara.manager.mapper.BizSceneRouteMapper;
import org.dromara.manager.service.IBizSceneRouteService;
import org.dromara.manager.utils.CoordinateConvertUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 场景路线Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizSceneRouteServiceImpl implements IBizSceneRouteService {

    private final BizSceneRouteMapper baseMapper;

    /**
     * 查询场景路线
     *
     * @param routeId 主键
     * @return 场景路线
     */
    @Override
    public BizSceneRouteVo queryById(Long routeId) {
        return baseMapper.selectVoById(routeId);
    }

    /**
     * 分页查询场景路线列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 场景路线分页列表
     */
    @Override
    public TableDataInfo<BizSceneRouteVo> queryPageList(BizSceneRouteBo bo, PageQuery pageQuery) {
        BizSceneRoute query = MapstructUtils.convert(bo, BizSceneRoute.class);
        Page<BizSceneRouteVo> result = baseMapper.selectSceneRoutePage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的场景路线列表
     *
     * @param bo 查询条件
     * @return 场景路线列表
     */
    @Override
    public List<BizSceneRouteVo> queryList(BizSceneRouteBo bo) {
        BizSceneRoute query = MapstructUtils.convert(bo, BizSceneRoute.class);
        return baseMapper.selectSceneRouteList(query);
    }

    /**
     * 新增场景路线
     *
     * @param bo 场景路线
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizSceneRouteBo bo) {
        BizSceneRoute add = MapstructUtils.convert(bo, BizSceneRoute.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRouteId(add.getRouteId());
        }
        return flag;
    }

    /**
     * 修改场景路线
     *
     * @param bo 场景路线
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizSceneRouteBo bo) {
        BizSceneRoute update = MapstructUtils.convert(bo, BizSceneRoute.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizSceneRoute entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
        if (StringUtils.isBlank(entity.getStrokeColor())) {
            entity.setStrokeColor("#0f766e");
        }
        if (StringUtils.isBlank(entity.getStrokeStyle())) {
            entity.setStrokeStyle("solid");
        }
        if (entity.getStrokeWeight() == null) {
            entity.setStrokeWeight(4);
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizSceneRoute entity) {
        if (!"solid".equals(entity.getStrokeStyle()) && !"dashed".equals(entity.getStrokeStyle())) {
            throw new ServiceException("线样式不正确");
        }
        if (!JSONUtil.isTypeJSONArray(entity.getGcj02Path())) {
            throw new ServiceException("路线范围格式不正确");
        }
        if (CoordinateConvertUtils.parsePath(entity.getGcj02Path()).size() < 2) {
            throw new ServiceException("路线范围至少需要两个坐标点");
        }
        entity.setBd09Path(CoordinateConvertUtils.convertPath(
            entity.getGcj02Path(),
            point -> CoordinateConvertUtils.gcj02ToBd09(point.lng(), point.lat())
        ));
        entity.setWgs84Path(CoordinateConvertUtils.convertPath(
            entity.getGcj02Path(),
            point -> CoordinateConvertUtils.gcj02ToWgs84(point.lng(), point.lat())
        ));

        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizSceneRoute>()
            .eq(BizSceneRoute::getRouteName, entity.getRouteName())
            .ne(entity.getRouteId() != null, BizSceneRoute::getRouteId, entity.getRouteId()));
        if (count > 0) {
            throw new ServiceException("路线名称已存在");
        }
    }

    /**
     * 校验并批量删除场景路线信息
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
