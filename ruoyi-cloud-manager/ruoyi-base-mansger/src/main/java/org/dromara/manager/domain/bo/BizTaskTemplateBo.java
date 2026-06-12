package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizTaskTemplate;

import java.util.List;

/**
 * 任务模板业务对象 biz_task_template
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizTaskTemplate.class, reverseConvertGenerate = false)
public class BizTaskTemplateBo extends BaseEntity {

    /**
     * 模板ID
     */
    @NotNull(message = "模板ID不能为空", groups = { EditGroup.class })
    private Long templateId;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空")
    @Size(max = 64, message = "模板编码长度不能超过{max}个字符")
    private String templateCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 100, message = "模板名称长度不能超过{max}个字符")
    private String templateName;

    /**
     * 路线ID
     */
    @NotNull(message = "路线不能为空")
    private Long routeId;

    /**
     * 任务说明
     */
    @Size(max = 1000, message = "任务说明长度不能超过{max}个字符")
    private String templateDesc;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

    /**
     * 编排点位
     */
    @Valid
    private List<BizTaskTemplatePointBo> points;

}
