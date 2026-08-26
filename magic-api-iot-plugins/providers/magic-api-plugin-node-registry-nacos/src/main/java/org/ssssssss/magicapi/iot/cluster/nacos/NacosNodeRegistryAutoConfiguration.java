package org.ssssssss.magicapi.iot.cluster.nacos;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

@AutoConfiguration
@EnableConfigurationProperties(NacosNodeRegistryProperties.class)
@ConditionalOnProperty(prefix = "iot.providers.node-registry", name = "type", havingValue = "nacos")
public class NacosNodeRegistryAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(NodeRegistry.class)
    NacosNodeRegistry nacosNodeRegistry(NacosNodeRegistryProperties properties) throws Exception {
        properties.validate();
        Properties clientProperties = new Properties();
        clientProperties.setProperty(PropertyKeyConst.SERVER_ADDR, properties.getServerAddr());
        if (properties.getNamespace() != null) clientProperties.setProperty(PropertyKeyConst.NAMESPACE, properties.getNamespace());
        if (properties.getUsername() != null) clientProperties.setProperty(PropertyKeyConst.USERNAME, properties.getUsername());
        if (properties.getPassword() != null) clientProperties.setProperty(PropertyKeyConst.PASSWORD, properties.getPassword());
        NamingService naming = NamingFactory.createNamingService(clientProperties);
        return new NacosNodeRegistry(naming, properties);
    }

    @Bean
    ProviderHealthIndicator nacosNodeRegistryHealth(NacosNodeRegistry registry,
                                                     NacosNodeRegistryProperties properties) {
        return new ProbeProviderHealthIndicator("node-registry", "nacos", Duration.ofSeconds(5), Duration.ofSeconds(3),
            () -> registry.isAvailable()
                ? ProbeProviderHealthIndicator.up(Map.of("service", properties.getServiceName()))
                : new PluginHealth(PluginHealth.Status.DOWN, "Nacos naming service is unavailable", Map.of()));
    }
}
