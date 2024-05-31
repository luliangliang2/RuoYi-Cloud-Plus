package org.dromara.workflow.api.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 流程定义配置模型 wf_definition_config
 *
 * @author may
 * @date 2024-03-18
 */
@Data
public class WfDefinitionConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 流程KEY
     */
    private String processKey;


    /**
     * 流程版本
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 表单管理
     */
    private WfFormManageDTO wfFormManageVo;


}
