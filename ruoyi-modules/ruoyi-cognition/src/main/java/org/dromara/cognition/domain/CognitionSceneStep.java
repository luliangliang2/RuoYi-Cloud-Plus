package org.dromara.cognition.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 认知场景步骤对象 cognition_scene_step
 *
 * @author zhang
 * @date 2025-10-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cognition_scene_step")
public class CognitionSceneStep extends TenantEntity {

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
     * 步骤序号
     */
    private Long stepOrder;

    /**
     * 步骤标题
     */
    private String title;

    /**
     * 步骤讲解
     */
    private String description;

    /**
     * 步骤图片
     */
    private Long imageId;

    /**
     * 步骤视频
     */
    private Long videoId;

    /**
     * 删除标志
     */
    @TableLogic
    private Long delFlag;


}
