package org.dromara.manager.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 任务模板点位动作业务对象
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
public class BizTaskTemplateActionBo {

    /**
     * 模板动作ID
     */
    private Long templateActionId;

    /**
     * 动作ID
     */
    @NotNull(message = "动作不能为空")
    private Long actionId;

    /**
     * 动作顺序
     */
    private Integer sequence;

    /**
     * 当前点位定制动作参数JSON
     */
    private String actionParams;

    /**
     * 备注
     */
    private String remark;

}
