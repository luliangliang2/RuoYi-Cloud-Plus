package org.dromara.common.mybatis.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.dromara.common.mybatis.helper.DataScopeCacheHelper;

/**
 * Dubbo DataScope缓存清理Filter
 *
 */
// 仅PROVIDER端生效，order=MAX_VALUE确保调用结束后最后执行
@Activate(group = {CommonConstants.PROVIDER}, order = Integer.MAX_VALUE)
public class DubboDataScopeCacheFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            return invoker.invoke(invocation);
        } finally {
            DataScopeCacheHelper.clearCache();
        }
    }
}
