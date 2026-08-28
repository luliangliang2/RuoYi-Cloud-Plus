package org.dromara.manager.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizRobotTaskAction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 机器人任务动作执行实例视图对象
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@AutoMapper(target = BizRobotTaskAction.class)
public class BizRobotTaskActionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskActionId;
    private Long taskId;
    private String taskNo;
    private Long taskPointId;
    private Integer loopNo;
    private Long pointId;
    private Integer pointSeq;
    private Long actionId;
    private String actionCode;
    private String actionName;
    private String actionType;
    private Integer actionSeq;
    private String actionParams;
    private String actionStatus;
    private Date startTime;
    private Date finishTime;
    private String reportPayload;
    private String errorMessage;
    private String remark;

}
