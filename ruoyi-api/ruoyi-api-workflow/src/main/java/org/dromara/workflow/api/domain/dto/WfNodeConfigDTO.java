package org.dromara.workflow.api.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 节点配置模型
 *
 * @author may
 * @date 2024-03-30
 */
@Data
public class WfNodeConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 流程定义id
     */
    private String definitionId;

    /**
     * 是否为申请人节点 （0是 1否）
     */
    private String applyUserTask;

    /**
     * 表单管理
     */
    private WfFormManageDTO wfFormManageVo;


}
