package org.dromara.manager.api;

import lombok.extern.slf4j.Slf4j;

/**
 * 维护树定义远程服务降级处理
 *
 * @author LionLi
 */
@Slf4j
public class RemoteTreeDefServiceMock implements RemoteTreeDefService {

    @Override
    public void syncTreeDef(String tenantId) {
        log.warn("维护树定义同步服务调用失败，tenantId={}", tenantId);
    }

}
