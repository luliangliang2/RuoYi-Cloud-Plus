package org.dromara.manager.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 机器人任务运行态状态。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
public class RobotTaskRuntimeStatusVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskId;
    private String taskNo;
    private String taskStatus;
    private Integer currentLoopNo;
    private Integer loopCount;
    private Integer currentPointSeq;
    private String currentPointName;
    private Integer currentActionSeq;
    private String currentActionName;
    private Integer progress;
    private Date lastReportTime;
    private String errorMessage;
    private Long vehicleId;
    private String vin;
    private String plateNo;

}
