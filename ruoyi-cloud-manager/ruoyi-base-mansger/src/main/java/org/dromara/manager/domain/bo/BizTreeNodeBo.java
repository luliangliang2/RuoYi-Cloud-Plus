package org.dromara.manager.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.domain.BizTreeNode;

/**
 * 维护树节点业务对象 biz_tree_node
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = BizTreeNode.class, reverseConvertGenerate = false)
public class BizTreeNodeBo extends BaseEntity {

    /**
     * 节点ID
     */
    @NotNull(message = "节点ID不能为空", groups = { EditGroup.class })
    private Long nodeId;

    /**
     * 树ID
     */
    @NotNull(message = "树ID不能为空")
    private Long treeId;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 节点编码
     */
    @NotBlank(message = "节点编码不能为空")
    @Size(max = 64, message = "节点编码长度不能超过{max}个字符")
    private String nodeCode;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空")
    @Size(max = 100, message = "节点名称长度不能超过{max}个字符")
    private String nodeName;

    /**
     * 节点类型
     */
    @Size(max = 32, message = "节点类型长度不能超过{max}个字符")
    private String nodeType;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    /**
     * 是否叶子节点（0否 1是）
     */
    private String leafFlag;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 扩展属性JSON
     */
    @Size(max = 2000, message = "扩展属性JSON长度不能超过{max}个字符")
    private String extJson;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过{max}个字符")
    private String remark;

}
