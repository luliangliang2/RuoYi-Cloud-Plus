package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizRobotTask;

import java.util.Date;
import java.util.List;

/**
 * 机器人任务执行业务对象 biz_robot_task
 *
 * @author LionLi
 * @date 2026-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizRobotTask.class, reverseConvertGenerate = false)
public class BizRobotTaskBo extends BaseEntity {

    @NotNull(message = "任务ID不能为空", groups = { EditGroup.class })
    private Long taskId;

    private String taskNo;

    @NotBlank(message = "任务名称不能为空")
    @Size(max = 100, message = "任务名称长度不能超过{max}个字符")
    private String taskName;

    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    private Long templateId;

    private Long routeId;

    @NotBlank(message = "车辆方式不能为空")
    private String assignMode;

    private Long vehicleId;

    private String vin;

    private String plateNo;

    private String loopFlag;

    private Integer loopCount;

    private String scheduleFlag;

    private Date startTime;

    private String taskStatus;

    private String remark;

    /**
     * 临时任务编排点位。模板任务为空时会从模板复制。
     */
    @Valid
    private List<BizTaskTemplatePointBo> points;

}
