package org.dromara.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class CaptureOriginalPathGatewayFilterFactory extends AbstractGatewayFilterFactory<CaptureOriginalPathGatewayFilterFactory.Config> {

    public CaptureOriginalPathGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 获取原始请求路径
            String originalPath = exchange.getRequest().getURI().getPath();
            // 将原始路径存储在请求头中
            ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header("X-Original-Path", originalPath)
                .build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
        // 可以在这里添加配置属性，如果需要的话
    }
}
