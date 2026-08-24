package org.dromara.manager.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Redis 中任务点位/动作运行态。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
public class RobotTaskStepRuntimeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskId;
    private String taskNo;
    private String stepType;
    private String stepKey;
    private Integer loopNo;
    private Long taskPointId;
    private Long pointId;
    private String pointName;
    private Integer pointSeq;
    private Long taskActionId;
    private Long actionId;
    private String actionCode;
    private String actionName;
    private Integer actionSeq;
    private String status;
    private String reportPayload;
    private String message;
    private Date startTime;
    private Date finishTime;
    private Date updateTime;

}
