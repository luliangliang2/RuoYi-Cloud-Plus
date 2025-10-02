package org.dromara.cognition.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.cognition.domain.CognitionSceneStep;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 认知场景步骤业务对象 cognition_scene_step
 *
 * @author zhang
 * @date 2025-10-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CognitionSceneStep.class, reverseConvertGenerate = false)
public class CognitionSceneStepBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
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


}
