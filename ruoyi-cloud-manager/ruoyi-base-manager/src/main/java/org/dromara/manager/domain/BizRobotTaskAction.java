package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 机器人任务动作执行实例对象 biz_robot_task_action
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_robot_task_action")
public class BizRobotTaskAction extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "task_action_id")
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

    @TableLogic
    private String delFlag;

    private String remark;

}
