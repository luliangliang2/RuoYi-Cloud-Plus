package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 任务模板点位动作对象 biz_task_template_point_action
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_task_template_point_action")
public class BizTaskTemplatePointAction extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模板动作ID
     */
    @TableId(value = "template_action_id")
    private Long templateActionId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板点位ID
     */
    private Long templatePointId;

    /**
     * 点位ID
     */
    private Long pointId;

    /**
     * 动作ID
     */
    private Long actionId;

    /**
     * 动作编码快照
     */
    private String actionCode;

    /**
     * 动作名称快照
     */
    private String actionName;

    /**
     * 动作类型快照
     */
    private String actionType;

    /**
     * 动作顺序
     */
    private Integer sequence;

    /**
     * 当前点位定制动作参数JSON
     */
    private String actionParams;

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
