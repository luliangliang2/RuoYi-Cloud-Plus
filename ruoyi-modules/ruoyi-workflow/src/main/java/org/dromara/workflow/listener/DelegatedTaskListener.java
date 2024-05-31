package org.dromara.workflow.listener;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.dubbo.generic.DubboGenericConsumer;
import org.dromara.workflow.api.RemoteTaskListener;
import org.dromara.workflow.api.domain.dto.TaskDTO;
import org.flowable.engine.impl.el.FixedValue;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 流程任务代理监听器，用于转发通知到业务服务
 *
 * @author may
 * @date 2023-12-12
 */
@Slf4j
@RequiredArgsConstructor
@Component("delegatedTaskListener")
public class DelegatedTaskListener implements TaskListener {

    /**
     * 流程定义的接口名
     */
    private FixedValue interfaceName;

    private final DubboGenericConsumer consumer;

    @Override
    public void notify(DelegateTask delegateTask) {

        String interfaceNameStr = this.interfaceName.getExpressionText();
        TaskDTO dto = BeanUtil.toBean(delegateTask, TaskDTO.class);

        // 调用远程服务
        Object result = consumer.call(interfaceNameStr, RemoteTaskListener.METHOD_NAME, dto);

        // 设置业务参数
        if (result instanceof Map map){
            delegateTask.setVariables(map);
        }
    }


}
