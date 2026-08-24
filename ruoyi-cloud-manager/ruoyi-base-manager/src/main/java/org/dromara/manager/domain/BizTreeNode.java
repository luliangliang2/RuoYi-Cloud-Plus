package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * 维护树节点对象 biz_tree_node
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_tree_node")
public class BizTreeNode extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @TableId(value = "node_id")
    private Long nodeId;

    /**
     * 树ID
     */
    private Long treeId;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 节点编码
     */
    private String nodeCode;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 层级
     */
    private Integer levelNo;

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
    private String extJson;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<BizTreeNode> children = new ArrayList<>();

}
