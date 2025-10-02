package org.dromara.cognition.domain.bo;

import org.dromara.cognition.domain.CognitionScene;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 认知场景业务对象 cognition_scene
 *
 * @author zhang
 * @date 2025-10-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CognitionScene.class, reverseConvertGenerate = false)
public class CognitionSceneBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 场景名称
     */
    @NotBlank(message = "场景名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sceneName;

    /**
     * 场景描述
     */
    private String description;

    /**
     * 封面图片
     */
    private Long coverImageId;


}
