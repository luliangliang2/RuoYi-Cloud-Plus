package com.ruoyi.common.dubbo.filter;

import cn.dev33.satoken.id.SaIdUtil;
import com.ruoyi.common.core.utils.JsonUtils;
import com.ruoyi.common.core.utils.SpringUtils;
import com.ruoyi.common.dubbo.enumd.RequestLogEnum;
import com.ruoyi.common.dubbo.properties.DubboCustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * dubbo日志过滤器
 *
 * @author Lion Li
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboRequestFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        DubboCustomProperties properties = SpringUtils.getBean(DubboCustomProperties.class);
        String client = CommonConstants.PROVIDER;
        if (RpcContext.getContext().isConsumerSide()) {
            client = CommonConstants.CONSUMER;
        }
        //region dubbo鉴权-Id-Token
        if (CommonConstants.PROVIDER.equals(client)) {
            //被调用端
            // 取出 Id-Token 进行校验
            String idToken = invocation.getAttachment(SaIdUtil.ID_TOKEN);
            SaIdUtil.checkToken(idToken);

            // 取出其他自定义附加数据
            // TenantContext tenantContext = invocation.getAttachment("tenantContext");
        } else if (CommonConstants.CONSUMER.equals(client)) {
            //调用端
            // 追加 Id-Token 参数
            RpcContext.getContext().setAttachment(SaIdUtil.ID_TOKEN, SaIdUtil.getToken());

            // 如果有其他自定义附加数据
            //RpcContext.getContext().setAttachment("tenantContext", "tenantContext");
        }
        //endregion
        if (!properties.getRequestLog()) {
            // 未开启则跳过日志逻辑
            return invoker.invoke(invocation);
        }
        String baselog = "Client[" + client + "],InterfaceName=[" + invocation.getInvoker().getInterface().getSimpleName() + "],MethodName=[" + invocation.getMethodName() + "]";
        if (properties.getLogLevel() == RequestLogEnum.INFO) {
            log.info("DUBBO - 服务调用: {}", baselog);
        } else {
            log.info("DUBBO - 服务调用: {},Parameter={}", baselog, invocation.getArguments());
        }

        long startTime = System.currentTimeMillis();
        // 执行接口调用逻辑
        Result result = invoker.invoke(invocation);
        // 调用耗时
        long elapsed = System.currentTimeMillis() - startTime;
        // 如果发生异常 则打印异常日志
        if (result.hasException() && invoker.getInterface().equals(GenericService.class)) {
            log.error("DUBBO - 服务异常: {},Exception={}", baselog, result.getException());
        } else {
            if (properties.getLogLevel() == RequestLogEnum.INFO) {
                log.info("DUBBO - 服务响应: {},SpendTime=[{}ms]", baselog, elapsed);
            } else if (properties.getLogLevel() == RequestLogEnum.FULL) {
                log.info("DUBBO - 服务响应: {},SpendTime=[{}ms],Response={}", baselog, elapsed, JsonUtils.toJsonString(new Object[]{result.getValue()}));
            }
        }
        return result;
    }

}
