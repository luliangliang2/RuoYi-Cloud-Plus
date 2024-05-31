package org.dromara.workflow.api;

import org.dromara.workflow.api.domain.dto.ExecutionDTO;

import java.util.Map;

/**
 * 流程实例监听器
 *
 * @Author ZETA
 * @Date 2024/5/30
 */
public interface RemoteExecutionListener {

    String METHOD_NAME = "notify";

    Map<String, Object> notify(ExecutionDTO execution);
}
