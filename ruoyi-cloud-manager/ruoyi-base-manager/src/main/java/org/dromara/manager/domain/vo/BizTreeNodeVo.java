package org.dromara.manager.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.domain.BizTreeNode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 维护树节点视图对象 biz_tree_node
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = BizTreeNode.class)
public class BizTreeNodeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @ExcelProperty(value = "节点ID")
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
    @ExcelProperty(value = "节点编码")
    private String nodeCode;

    /**
     * 节点名称
     */
    @ExcelProperty(value = "节点名称")
    private String nodeName;

    /**
     * 节点类型
     */
    @ExcelProperty(value = "节点类型")
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
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 扩展属性JSON
     */
    private String extJson;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
