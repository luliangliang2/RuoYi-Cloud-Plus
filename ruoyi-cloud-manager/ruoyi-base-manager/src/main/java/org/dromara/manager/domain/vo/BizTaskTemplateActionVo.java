package org.dromara.manager.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务模板点位动作视图对象
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
public class BizTaskTemplateActionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long templateActionId;
    private Long templatePointId;
    private Long pointId;
    private Long actionId;
    private String actionCode;
    private String actionName;
    private String actionType;
    private Integer sequence;
    private String actionParams;
    private String remark;

}
