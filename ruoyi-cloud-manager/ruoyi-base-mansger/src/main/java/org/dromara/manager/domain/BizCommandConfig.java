package org.dromara.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 指令配置对象 biz_command_config
 *
 * @author LionLi
 * @date 2026-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_command_config")
public class BizCommandConfig extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 指令ID
     */
    @TableId(value = "command_id")
    private Long commandId;

    /**
     * 分类树ID
     */
    private Long treeId;

    /**
     * 分类节点ID
     */
    private Long categoryNodeId;

    /**
     * 指令编码
     */
    private String commandCode;

    /**
     * 指令名称
     */
    private String commandName;

    /**
     * 指令类型（single单指令 multiple多指令）
     */
    private String commandType;

    /**
     * 指令JSON模板
     */
    private String commandTemplate;

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
