package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizRobotAction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 机器人动作定义视图对象 biz_robot_action_def
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizRobotAction.class)
public class BizRobotActionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 动作ID
     */
    @ExcelProperty(value = "动作ID")
    private Long actionId;

    /**
     * 动作唯一编码
     */
    @ExcelProperty(value = "动作编码")
    private String actionCode;

    /**
     * 动作名称
     */
    @ExcelProperty(value = "动作名称")
    private String actionName;

    /**
     * 动作类型（trigger触发一次 continuous持续动作）
     */
    @ExcelProperty(value = "动作类型")
    private String actionType;

    /**
     * 动作参数模板JSON
     */
    private String paramsTemplate;

    /**
     * 动作描述
     */
    @ExcelProperty(value = "动作描述")
    private String description;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    private Integer sortOrder;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
