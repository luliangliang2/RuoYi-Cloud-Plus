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
import org.dromara.manager.domain.BizSceneArea;
import org.dromara.manager.domain.bo.BizSceneAreaBo;
import org.dromara.manager.domain.vo.BizSceneAreaVo;
import org.dromara.manager.mapper.BizSceneAreaMapper;
import org.dromara.manager.service.IBizSceneAreaService;
import org.dromara.manager.utils.CoordinateConvertUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 场景区域Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BizSceneAreaServiceImpl implements IBizSceneAreaService {

    private final BizSceneAreaMapper baseMapper;

    /**
     * 查询场景区域
     *
     * @param areaId 主键
     * @return 场景区域
     */
    @Override
    public BizSceneAreaVo queryById(Long areaId) {
        return baseMapper.selectVoById(areaId);
    }

    /**
     * 分页查询场景区域列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 场景区域分页列表
     */
    @Override
    public TableDataInfo<BizSceneAreaVo> queryPageList(BizSceneAreaBo bo, PageQuery pageQuery) {
        BizSceneArea query = MapstructUtils.convert(bo, BizSceneArea.class);
        Page<BizSceneAreaVo> result = baseMapper.selectSceneAreaPage(pageQuery.build(), query);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的场景区域列表
     *
     * @param bo 查询条件
     * @return 场景区域列表
     */
    @Override
    public List<BizSceneAreaVo> queryList(BizSceneAreaBo bo) {
        BizSceneArea query = MapstructUtils.convert(bo, BizSceneArea.class);
        return baseMapper.selectSceneAreaList(query);
    }

    /**
     * 新增场景区域
     *
     * @param bo 场景区域
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(BizSceneAreaBo bo) {
        BizSceneArea add = MapstructUtils.convert(bo, BizSceneArea.class);
        fillDefaultValue(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setAreaId(add.getAreaId());
        }
        return flag;
    }

    /**
     * 修改场景区域
     *
     * @param bo 场景区域
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(BizSceneAreaBo bo) {
        BizSceneArea update = MapstructUtils.convert(bo, BizSceneArea.class);
        fillDefaultValue(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void fillDefaultValue(BizSceneArea entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("0");
        }
        if (StringUtils.isBlank(entity.getFillColor())) {
            entity.setFillColor("#14b8a6");
        }
        if (StringUtils.isBlank(entity.getStrokeColor())) {
            entity.setStrokeColor("#0f766e");
        }
        if (StringUtils.isBlank(entity.getStrokeStyle())) {
            entity.setStrokeStyle("solid");
        }
        if (entity.getStrokeWeight() == null) {
            entity.setStrokeWeight(2);
        }
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(BizSceneArea entity) {
        if (!"solid".equals(entity.getStrokeStyle()) && !"dashed".equals(entity.getStrokeStyle())) {
            throw new ServiceException("边界线样式不正确");
        }
        if (!JSONUtil.isTypeJSONArray(entity.getGcj02Path())) {
            throw new ServiceException("场景范围格式不正确");
        }
        if (CoordinateConvertUtils.parsePath(entity.getGcj02Path()).size() < 3) {
            throw new ServiceException("场景范围至少需要三个坐标点");
        }
        entity.setBd09Path(CoordinateConvertUtils.convertPath(
            entity.getGcj02Path(),
            point -> CoordinateConvertUtils.gcj02ToBd09(point.lng(), point.lat())
        ));
        entity.setWgs84Path(CoordinateConvertUtils.convertPath(
            entity.getGcj02Path(),
            point -> CoordinateConvertUtils.gcj02ToWgs84(point.lng(), point.lat())
        ));

        Long count = baseMapper.selectCount(new LambdaQueryWrapper<BizSceneArea>()
            .eq(BizSceneArea::getAreaName, entity.getAreaName())
            .ne(entity.getAreaId() != null, BizSceneArea::getAreaId, entity.getAreaId()));
        if (count > 0) {
            throw new ServiceException("区域名称已存在");
        }
    }

    /**
     * 校验并批量删除场景区域信息
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
