package org.dromara.workflow.api;

import org.dromara.workflow.api.domain.dto.ProcessInstanceDTO;

import java.util.List;

/**
 * 流程实例Service接口
 *
 * @Author ZETA
 * @Date 2024/5/29
 */
public interface RemoteActHiProcinstService{

    /**
     * 按照业务id查询
     *
     * @param businessKeys 业务id
     * @return 结果
     */
    List<ProcessInstanceDTO> getProcessInstances(List<String> businessKeys);

    /**
     * 按照业务id查询
     *
     * @param businessKey 业务id
     * @return 结果
     */
    ProcessInstanceDTO getProcessInstance(String businessKey);
}
