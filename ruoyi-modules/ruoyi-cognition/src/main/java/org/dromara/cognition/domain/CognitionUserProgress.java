package org.dromara.cognition.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户场景学习进度对象 cognition_user_progress
 *
 * @author zhang
 * @date 2025-10-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cognition_user_progress")
public class CognitionUserProgress extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 场景
     */
    private Long sceneId;

    /**
     * 当前步骤
     */
    private Long stepId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 是否完成
     */
    private Long isCompleted;

    /**
     * 删除标志
     */
    @TableLogic
    private Long delFlag;


}
