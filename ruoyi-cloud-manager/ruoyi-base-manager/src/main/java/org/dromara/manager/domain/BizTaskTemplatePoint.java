package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 任务模板点位编排对象 biz_task_template_point
 *
 * @author LionLi
 * @date 2026-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_task_template_point")
public class BizTaskTemplatePoint extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模板点位ID
     */
    @TableId(value = "template_point_id")
    private Long templatePointId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 路线ID
     */
    private Long routeId;

    /**
     * 点位ID
     */
    private Long pointId;

    /**
     * 点位名称快照
     */
    private String pointName;

    /**
     * 到达顺序
     */
    private Integer sequence;

    /**
     * 是否必须到达（0否 1是）
     */
    private String requiredFlag;

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
