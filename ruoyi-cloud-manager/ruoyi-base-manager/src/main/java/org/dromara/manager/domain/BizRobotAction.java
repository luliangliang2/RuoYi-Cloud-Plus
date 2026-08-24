package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 机器人动作定义对象 biz_robot_action_def
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_robot_action_def")
public class BizRobotAction extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 动作ID
     */
    @TableId(value = "action_id")
    private Long actionId;

    /**
     * 动作唯一编码
     */
    private String actionCode;

    /**
     * 动作名称
     */
    private String actionName;

    /**
     * 动作类型（trigger触发一次 continuous持续动作）
     */
    private String actionType;

    /**
     * 动作参数模板JSON
     */
    private String paramsTemplate;

    /**
     * 动作描述
     */
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
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

}
