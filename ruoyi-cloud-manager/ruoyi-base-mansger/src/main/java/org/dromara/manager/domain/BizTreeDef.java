package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 维护树定义对象 biz_tree_def
 *
 * @author LionLi
 * @date 2026-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_tree_def")
public class BizTreeDef extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 树ID
     */
    @TableId(value = "tree_id")
    private Long treeId;

    /**
     * 树编码
     */
    private String treeCode;

    /**
     * 树名称
     */
    private String treeName;

    /**
     * 树类型
     */
    private String treeType;

    /**
     * 使用模块编码
     */
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
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

}
