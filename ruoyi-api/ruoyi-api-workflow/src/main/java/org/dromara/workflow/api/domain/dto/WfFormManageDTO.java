package org.dromara.workflow.api.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 表单管理模型
 *
 * @author may
 * @date 2024-03-29
 */
@Data
public class WfFormManageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 路由地址/表单ID
     */
    private String router;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户编号
     */
    private String tenantId;
}
