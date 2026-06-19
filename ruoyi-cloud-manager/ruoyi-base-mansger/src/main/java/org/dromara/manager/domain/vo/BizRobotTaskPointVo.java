package org.dromara.manager.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizRobotTaskPoint;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 机器人任务点位执行实例视图对象
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@AutoMapper(target = BizRobotTaskPoint.class)
public class BizRobotTaskPointVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    private String remark;
    private List<BizRobotTaskActionVo> actions;

}
