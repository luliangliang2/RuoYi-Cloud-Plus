package org.dromara.common.tenant.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.tenant.helper.TenantHelper;

/**
 * @author lishuyan
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER}, order = Integer.MIN_VALUE)
public class DubboTenantProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (Boolean.FALSE.equals(TenantHelper.isEnable())) {
            // 未开启多租户则跳过获取参数逻辑
            return invoker.invoke(invocation);
        }
        String tenantId = invocation.getAttachment(LoginHelper.TENANT_KEY);
        if (StringUtils.isNotEmpty(tenantId)) {
            log.debug("DUBBO - 服务调用-获取租户ID: {}", tenantId);
            TenantHelper.setRpcTenantId(tenantId);
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            TenantHelper.clear();
        }
    }
}
