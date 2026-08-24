package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizRobotAction;

/**
 * 机器人动作定义业务对象 biz_robot_action_def
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizRobotAction.class, reverseConvertGenerate = false)
public class BizRobotActionBo extends BaseEntity {

    /**
     * 动作ID
     */
    @NotNull(message = "动作ID不能为空", groups = { EditGroup.class })
    private Long actionId;

    /**
     * 动作唯一编码
     */
    @NotBlank(message = "动作编码不能为空")
    @Size(max = 64, message = "动作编码长度不能超过{max}个字符")
    private String actionCode;

    /**
     * 动作名称
     */
    @NotBlank(message = "动作名称不能为空")
    @Size(max = 100, message = "动作名称长度不能超过{max}个字符")
    private String actionName;

    /**
     * 动作类型（trigger触发一次 continuous持续动作）
     */
    @NotBlank(message = "动作类型不能为空")
    @Size(max = 32, message = "动作类型长度不能超过{max}个字符")
    private String actionType;

    /**
     * 动作参数模板JSON
     */
    private String paramsTemplate;

    /**
     * 动作描述
     */
    @Size(max = 500, message = "动作描述长度不能超过{max}个字符")
    private String description;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
