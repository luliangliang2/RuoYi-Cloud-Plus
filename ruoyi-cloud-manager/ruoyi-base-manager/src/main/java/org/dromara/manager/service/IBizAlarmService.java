package org.dromara.manager.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.domain.bo.BizAlarmBo;
import org.dromara.manager.domain.vo.BizAlarmVo;

import java.util.List;

/**
 * 告警Service接口
 *
 * @author LionLi
 * @date 2026-05-28
 */
public interface IBizAlarmService {

    /**
     * 分页查询告警列表
     *
     * @param bo 查询条件
     * @param pageQuery 分页参数
     * @return 告警分页列表
     */
    TableDataInfo<BizAlarmVo> queryPageList(BizAlarmBo bo, PageQuery pageQuery);

    /**
     * 查询告警列表
     *
     * @param bo 查询条件
     * @return 告警列表
     */
    List<BizAlarmVo> queryList(BizAlarmBo bo);

}
