package org.dromara.manager.api;

/**
 * 维护树定义远程服务
 *
 * @author LionLi
 */
public interface RemoteTreeDefService {

    /**
     * 同步默认租户的维护树定义到指定租户
     *
     * @param tenantId 租户编号
     */
    void syncTreeDef(String tenantId);

}
