package org.dromara.demo.listener.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.demo.listener.TestLeaveTaskListener;
import org.dromara.workflow.api.RemoteTaskListener;
import org.dromara.workflow.api.domain.dto.TaskDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 流程任务监听测试
 *
 * @author may
 * @date 2023-12-12
 */
@Slf4j
@Component("testLeaveTaskListener")
@DubboService
public class TestLeaveTaskListenerImpl implements TestLeaveTaskListener {

    @Override
    public Map<String, Object> notify(TaskDTO task) {
        log.info("执行监听【" + task.getName() + "】");
        return null;
    }
}
