package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 机器人任务点位执行实例对象 biz_robot_task_point
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_robot_task_point")
public class BizRobotTaskPoint extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "task_point_id")
    private Long taskPointId;

    private Long taskId;
    private String taskNo;
    private Integer loopNo;
    private Long routeId;
    private Long pointId;
    private String pointName;
    private Integer pointSeq;
    private String requiredFlag;
    private BigDecimal gcj02Lng;
    private BigDecimal gcj02Lat;
    private BigDecimal bd09Lng;
    private BigDecimal bd09Lat;
    private BigDecimal wgs84Lng;
    private BigDecimal wgs84Lat;
    private String pointStatus;
    private Date arriveTime;
    private Date finishTime;
    private String reportPayload;

    @TableLogic
    private String delFlag;

    private String remark;

}
