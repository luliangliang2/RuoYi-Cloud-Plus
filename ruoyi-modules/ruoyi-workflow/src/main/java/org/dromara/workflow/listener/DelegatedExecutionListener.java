package org.dromara.workflow.listener;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.dubbo.generic.DubboGenericConsumer;
import org.dromara.workflow.api.RemoteExecutionListener;
import org.dromara.workflow.api.domain.dto.ExecutionDTO;
import org.dromara.workflow.api.domain.dto.TaskDTO;
import org.dromara.workflow.utils.QueryUtils;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 流程实例代理监听器，用于转发通知到业务服务
 *
 * @author may
 * @date 2023-12-12
 */
@Slf4j
@RequiredArgsConstructor
@Component("delegatedExecutionListener")
public class DelegatedExecutionListener implements ExecutionListener {

    private final DubboGenericConsumer consumer;

    @Override
    public void notify(DelegateExecution execution) {

        // 获取远端接口
        List<FieldExtension> fieldExtensions = execution.getCurrentFlowableListener().getFieldExtensions();
        FieldExtension clazzExtension = fieldExtensions.stream()
            .filter(fieldExtension -> fieldExtension.getFieldName().equals("interfaceName"))
            .findFirst().orElse(null);
        if (null == clazzExtension){
            throw new RuntimeException("获取参数失败，远端接口配置不存在。");
        }

        String interfaceName = clazzExtension.getStringValue();
        // 构造参数
        ExecutionDTO executionDTO = BeanUtil.copyProperties(execution, ExecutionDTO.class);
        Map<String, Object> variables = execution.getVariables();
        executionDTO.setVariables(variables);

        // 调用远程服务
        Object result = consumer.call(interfaceName, RemoteExecutionListener.METHOD_NAME, executionDTO);

        // 设置业务参数
        if (result instanceof Map map){
            execution.setVariables(map);
        }
    }
}
