package org.dromara.manager.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizTaskTemplatePointAction;
import org.dromara.manager.domain.vo.BizTaskTemplateActionVo;

import java.util.List;

/**
 * 任务模板点位动作Mapper接口
 *
 * @author LionLi
 * @date 2026-06-12
 */
public interface BizTaskTemplatePointActionMapper extends BaseMapperPlus<BizTaskTemplatePointAction, BizTaskTemplateActionVo> {

    /**
     * 查询模板动作列表
     *
     * @param templateId 模板ID
     * @return 模板动作列表
     */
    List<BizTaskTemplateActionVo> selectByTemplateId(@Param("templateId") Long templateId);

}
