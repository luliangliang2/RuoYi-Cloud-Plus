package org.dromara.manager.domain.bo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 任务模板点位编排业务对象
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
public class BizTaskTemplatePointBo {

    /**
     * 模板点位ID
     */
    private Long templatePointId;

    /**
     * 点位ID
     */
    @NotNull(message = "点位不能为空")
    private Long pointId;

    /**
     * 到达顺序
     */
    private Integer sequence;

    /**
     * 是否必须到达（0否 1是）
     */
    private String requiredFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 点位动作列表
     */
    @Valid
    private List<BizTaskTemplateActionBo> actions;

}
