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
 * 机器人任务执行对象 biz_robot_task
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_robot_task")
public class BizRobotTask extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "task_id")
    private Long taskId;

    private String taskNo;
    private String taskName;
    private String taskType;
    private Long templateId;
    private Long routeId;
    private String assignMode;
    private Long vehicleId;
    private String vin;
    private String plateNo;
    private String loopFlag;
    private Integer loopCount;
    private String scheduleFlag;
    private Date startTime;
    private Date actualStartTime;
    private Date finishTime;
    private String taskStatus;
    private Integer currentLoopNo;
    private Integer currentPointSeq;
    private Integer currentActionSeq;
    private String commandJson;
    private String errorMessage;

    @TableLogic
    private String delFlag;

    private String remark;

}
