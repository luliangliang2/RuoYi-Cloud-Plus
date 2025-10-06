package org.dromara.cognition.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.constant.TransConstant;

import java.io.Serial;

/**
 * 认知场景对象 cognition_scene
 *
 * @author zhang
 * @date 2025-10-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cognition_scene")
public class CognitionScene extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 场景描述
     */
    private String description;

    /**
     * 封面图片
     */
    private Long coverImageId;

    /**
     * 删除标志
     */
    @TableLogic
    private Long delFlag;


}
