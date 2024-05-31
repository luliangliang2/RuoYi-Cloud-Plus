package org.dromara.demo.listener.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.demo.listener.TestLeaveExecutionListener;
import org.dromara.workflow.api.domain.dto.ExecutionDTO;

import java.util.Map;

/**
 * 流程实例监听测试
 *
 * @author may
 * @date 2023-12-12
 */
@Slf4j
@RequiredArgsConstructor
@DubboService
public class TestLeaveExecutionListenerImpl implements TestLeaveExecutionListener {
    @Override
    public Map<String, Object> notify(ExecutionDTO execution) {
                log.info("执行监听【" + execution.getName() + "】");
        return Map.of("testkey", "testValue");
    }
}
