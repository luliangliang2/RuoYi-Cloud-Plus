package org.dromara.workflow.api;

import org.dromara.workflow.api.domain.dto.TaskDTO;

import java.util.Map;

/**
 * 流程任务监听器
 *
 * @Author ZETA
 * @Date 2024/5/30
 */
public  interface RemoteTaskListener {

    String METHOD_NAME = "notify";

    /**
     * 执行监听
     *
     * @param task 任务
     * @return
     */
    Map<String, Object> notify(TaskDTO task);
}
