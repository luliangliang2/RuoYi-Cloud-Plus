package org.dromara.manager.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizAlarmBo;
import org.dromara.manager.domain.vo.BizAlarmVo;
import org.dromara.manager.mapper.BizAlarmMapper;
import org.dromara.manager.mapper.BizVehicleMapper;
import org.dromara.manager.service.IBizAlarmService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警Service业务层处理
 *
 * @author LionLi
 * @date 2026-05-28
 */
@RequiredArgsConstructor
@Service
public class BizAlarmServiceImpl implements IBizAlarmService {

    private static final int EXPORT_LIMIT = 10000;

    private final BizAlarmMapper baseMapper;
    private final BizVehicleMapper vehicleMapper;

    /**
     * 分页查询告警列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 告警分页列表
     */
    @Override
    public TableDataInfo<BizAlarmVo> queryPageList(BizAlarmBo bo, PageQuery pageQuery) {
        List<String> vins = vehicleMapper.selectAuthorizedVinList(bo);
        if (CollUtil.isEmpty(vins)) {
            return new TableDataInfo<>(List.of(), 0);
        }

        long total = ObjectUtil.defaultIfNull(baseMapper.selectAlarmCount(bo, vins), 0L);
        if (total <= 0) {
            return new TableDataInfo<>(List.of(), 0);
        }

        int pageNum = ObjectUtil.defaultIfNull(pageQuery.getPageNum(), PageQuery.DEFAULT_PAGE_NUM);
        int pageSize = ObjectUtil.defaultIfNull(pageQuery.getPageSize(), PageQuery.DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = PageQuery.DEFAULT_PAGE_NUM;
        }
        if (pageSize <= 0) {
            pageSize = PageQuery.DEFAULT_PAGE_SIZE;
        }
        long offset = (long) (pageNum - 1) * pageSize;
        List<BizAlarmVo> rows = baseMapper.selectAlarmList(bo, vins, offset, pageSize);
        return new TableDataInfo<>(rows, total);
    }

    /**
     * 查询告警列表
     *
     * @param bo 查询条件
     * @return 告警列表
     */
    @Override
    public List<BizAlarmVo> queryList(BizAlarmBo bo) {
        List<String> vins = vehicleMapper.selectAuthorizedVinList(bo);
        if (CollUtil.isEmpty(vins)) {
            return List.of();
        }
        return baseMapper.selectAlarmList(bo, vins, 0, EXPORT_LIMIT);
    }

}
