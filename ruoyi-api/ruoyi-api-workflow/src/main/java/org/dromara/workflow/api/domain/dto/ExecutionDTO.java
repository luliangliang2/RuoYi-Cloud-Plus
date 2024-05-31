package org.dromara.workflow.api.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 任务模型
 *
 * @author may
 */
@Data
public class ExecutionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 执行实例id
     */
    private String id;

    /**
     * 执行实例名称
     */
    private String name;

    /**
     * 执行实例事件名称
     */
    private String eventName;

    /**
     * 状态
     */
    private String suspensionState;

    /**
     * 描述
     */
    private String description;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 负责此任务的人员的用户id
     */
    private String owner;

    /**
     * 办理人id
     */
    private Long assignee;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 无用
     */
    private String taskDefinitionId;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 已办任务-创建时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 节点id
     */
    private String taskDefinitionKey;

    /**
     * 任务截止日期
     */
    private Date dueDate;

    /**
     * 流程类别
     */
    private String category;

    /**
     * 父级任务id
     */
    private String parentTaskId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 认领时间
     */
    private Date claimTime;

    /**
     * 流程状态
     */
    private String businessStatus;

    /**
     * 流程状态
     */
    private String businessStatusName;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程定义key
     */
    private String processDefinitionKey;

    /**
     * 流程定义版本
     */
    private Integer processDefinitionVersion;

    /**
     * 是否会签
     */
    private Boolean multiInstance;

    /**
     * 业务id
     */
    private String businessKey;

    private Map<String, Object> variables;
}
