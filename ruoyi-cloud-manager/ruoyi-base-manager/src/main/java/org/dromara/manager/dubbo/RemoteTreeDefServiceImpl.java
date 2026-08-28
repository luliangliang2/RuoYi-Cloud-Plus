package org.dromara.manager.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.manager.api.RemoteTreeDefService;
import org.dromara.manager.service.IBizTreeDefService;
import org.springframework.stereotype.Service;

/**
 * 维护树定义远程服务
 *
 * @author LionLi
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteTreeDefServiceImpl implements RemoteTreeDefService {

    private final IBizTreeDefService treeDefService;

    @Override
    public void syncTreeDef(String tenantId) {
        treeDefService.syncTreeDef(tenantId);
    }

}
