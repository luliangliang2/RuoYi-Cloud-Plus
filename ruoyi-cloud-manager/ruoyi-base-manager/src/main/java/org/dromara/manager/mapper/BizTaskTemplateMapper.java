package org.dromara.manager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.domain.BizTaskTemplate;
import org.dromara.manager.domain.vo.BizTaskTemplateVo;

import java.util.List;

/**
 * 任务模板Mapper接口
 *
 * @author LionLi
 * @date 2026-06-12
 */
public interface BizTaskTemplateMapper extends BaseMapperPlus<BizTaskTemplate, BizTaskTemplateVo> {

    /**
     * 查询任务模板列表
     *
     * @param bo 查询条件
     * @return 任务模板列表
     */
    List<BizTaskTemplateVo> selectTaskTemplateList(@Param("bo") BizTaskTemplate bo);

    /**
     * 分页查询任务模板列表
     *
     * @param page 分页对象
     * @param bo 查询条件
     * @return 任务模板列表
     */
    Page<BizTaskTemplateVo> selectTaskTemplatePage(@Param("page") Page<BizTaskTemplateVo> page, @Param("bo") BizTaskTemplate bo);

}
