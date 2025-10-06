package org.dromara.cognition.domain.bo;

import org.dromara.cognition.domain.CognitionUserProgress;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 用户场景学习进度业务对象 cognition_user_progress
 *
 * @author zhang
 * @date 2025-10-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CognitionUserProgress.class, reverseConvertGenerate = false)
public class CognitionUserProgressBo extends BaseEntity {

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


}
