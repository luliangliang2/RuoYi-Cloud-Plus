package org.ssssssss.magicapi.iot.cluster;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

@AutoConfiguration
@EnableConfigurationProperties(GatewayClusterProperties.class)
@ConditionalOnProperty(prefix = "iot.cluster", name = "enabled", havingValue = "true")
public class GatewayClusterAutoConfiguration {
    @Bean
    GatewayNodeCoordinator gatewayNodeCoordinator(NodeRegistry registry, GatewayClusterProperties properties) {
        return new GatewayNodeCoordinator(registry, properties);
    }
}
