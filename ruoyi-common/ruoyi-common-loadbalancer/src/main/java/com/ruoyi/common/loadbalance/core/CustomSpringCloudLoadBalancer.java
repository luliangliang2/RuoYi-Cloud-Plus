package com.ruoyi.common.loadbalance.core;

import cn.hutool.core.net.NetUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 自定义 SpringCloud 负载均衡算法
 *
 * @author Lion Li
 */
@Slf4j
@AllArgsConstructor
public class CustomSpringCloudLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final String serviceId;

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        RequestDataContext requestDataContext = (RequestDataContext) request.getContext();
        String clientIp = requestDataContext.getClientRequest().getHeaders().getFirst("Client-IP");
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, clientIp));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances,
                                                              String clientIp) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, clientIp);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, String clientIp) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        for (ServiceInstance instance : instances) {
            if (NetUtil.localIpv4s().contains(instance.getHost())) {
                // 如果此实例是本地地址，则使用该地址。适用于本地开发启动了gateway服务的情况下
                return new DefaultResponse(instance);
            } else if (Objects.equals(clientIp, instance.getHost())) {
                // 如果此实例地址与客户端地址一致，则使用该地址。
                // 适用于团队开发情况下，本地无需启动gateway，根据请求来源IP定位到本机的服务
                return new DefaultResponse(instance);
            }
        }
        return new DefaultResponse(instances.get(ThreadLocalRandom.current().nextInt(instances.size())));
    }

}
