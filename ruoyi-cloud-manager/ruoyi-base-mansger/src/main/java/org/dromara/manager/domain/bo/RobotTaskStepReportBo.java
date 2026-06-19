package org.dromara.manager.domain.bo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 机器人任务步骤上报对象。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
public class RobotTaskStepReportBo {

    private Long taskId;

    private String taskNo;

    private Long vehicleId;

    private String vin;

    private Integer loopNo;

    private Integer pointSeq;

    private Integer actionSeq;

    /**
     * point/action，默认 action。
     */
    private String stepType;

    @NotBlank(message = "步骤状态不能为空")
    private String status;

    private String message;

    private String reportPayload;

}
