package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 分类业务绑定对象 biz_tree_category_bind
 *
 * @author LionLi
 * @date 2026-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_tree_category_bind")
public class BizTreeCategoryBind extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 绑定ID
     */
    @TableId(value = "bind_id")
    private Long bindId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long nodeId;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

}
