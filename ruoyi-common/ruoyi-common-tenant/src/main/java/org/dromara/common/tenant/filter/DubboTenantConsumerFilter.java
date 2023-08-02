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
@Activate(group = {CommonConstants.CONSUMER}, order = Integer.MIN_VALUE)
public class DubboTenantConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (Boolean.FALSE.equals(TenantHelper.isEnable())) {
            // 未开启多租户则跳过参数传递逻辑
            return invoker.invoke(invocation);
        }
        String tenantId = TenantHelper.getRpcTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            log.debug("DUBBO - 服务调用-传递租户ID: {}", tenantId);
            invocation.setAttachment(LoginHelper.TENANT_KEY, tenantId);
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            TenantHelper.clear();
        }
    }
}
