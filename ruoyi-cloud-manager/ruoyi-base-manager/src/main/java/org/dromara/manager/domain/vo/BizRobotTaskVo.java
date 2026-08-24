package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizRobotTask;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 机器人任务执行视图对象 biz_robot_task
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizRobotTask.class)
public class BizRobotTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "任务ID")
    private Long taskId;

    @ExcelProperty(value = "任务编号")
    private String taskNo;

    @ExcelProperty(value = "任务名称")
    private String taskName;

    @ExcelProperty(value = "任务类型")
    private String taskType;

    private Long templateId;

    @ExcelProperty(value = "模板名称")
    private String templateName;

    private Long routeId;

    @ExcelProperty(value = "路线名称")
    private String routeName;

    @ExcelProperty(value = "车辆方式")
    private String assignMode;

    private Long vehicleId;

    @ExcelProperty(value = "VIN")
    private String vin;

    @ExcelProperty(value = "车牌号")
    private String plateNo;

    @ExcelProperty(value = "是否循环")
    private String loopFlag;

    @ExcelProperty(value = "循环次数")
    private Integer loopCount;

    @ExcelProperty(value = "是否定时")
    private String scheduleFlag;

    @ExcelProperty(value = "计划开始时间")
    private Date startTime;

    private Date actualStartTime;

    private Date finishTime;

    @ExcelProperty(value = "任务状态")
    private String taskStatus;

    private Integer currentLoopNo;

    private Integer currentPointSeq;

    private Integer currentActionSeq;

    private String commandJson;

    private String errorMessage;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    private String remark;

    private List<BizRobotTaskPointVo> points;

}
