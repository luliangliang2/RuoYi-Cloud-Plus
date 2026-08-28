package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizTaskTemplatePoint;
import org.dromara.manager.domain.vo.BizTaskTemplatePointVo;

import java.util.List;

/**
 * 任务模板点位编排Mapper接口
 *
 * @author LionLi
 * @date 2026-06-12
 */
public interface BizTaskTemplatePointMapper extends BaseMapperPlus<BizTaskTemplatePoint, BizTaskTemplatePointVo> {

    /**
     * 查询模板点位列表
     *
     * @param templateId 模板ID
     * @return 模板点位列表
     */
    List<BizTaskTemplatePointVo> selectByTemplateId(@Param("templateId") Long templateId);

}
