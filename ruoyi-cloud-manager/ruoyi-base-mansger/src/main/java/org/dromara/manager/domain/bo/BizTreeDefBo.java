package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizTreeDef;

/**
 * 维护树定义业务对象 biz_tree_def
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizTreeDef.class, reverseConvertGenerate = false)
public class BizTreeDefBo extends BaseEntity {

    /**
     * 树ID
     */
    @NotNull(message = "树ID不能为空", groups = { EditGroup.class })
    private Long treeId;

    /**
     * 树编码
     */
    @NotBlank(message = "树编码不能为空")
    @Size(max = 64, message = "树编码长度不能超过{max}个字符")
    private String treeCode;

    /**
     * 树名称
     */
    @NotBlank(message = "树名称不能为空")
    @Size(max = 100, message = "树名称长度不能超过{max}个字符")
    private String treeName;

    /**
     * 树类型
     */
    @Size(max = 32, message = "树类型长度不能超过{max}个字符")
    private String treeType;

    /**
     * 选择模式（single单选 multiple多选）
     */
    @Size(max = 16, message = "选择模式长度不能超过{max}个字符")
    private String selectMode;

    /**
     * 使用模块编码
     */
    @Size(max = 64, message = "使用模块编码长度不能超过{max}个字符")
    private String moduleCode;

    /**
     * 根节点模式（1单根 2多根）
     */
    private String rootMode;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
