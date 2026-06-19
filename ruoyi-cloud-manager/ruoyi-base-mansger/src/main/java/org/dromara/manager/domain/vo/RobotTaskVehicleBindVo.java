package org.dromara.manager.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Redis 中任务与车辆的运行绑定关系。
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
public class RobotTaskVehicleBindVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskId;
    private String taskNo;
    private String taskName;
    private String taskStatus;
    private Long vehicleId;
    private String vin;
    private String plateNo;
    private String bindType;
    private Date bindTime;

}
