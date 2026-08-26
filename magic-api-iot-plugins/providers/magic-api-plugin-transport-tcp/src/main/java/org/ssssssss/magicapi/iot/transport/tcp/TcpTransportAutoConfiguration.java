package org.ssssssss.magicapi.iot.transport.tcp;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(TcpTransportProperties.class)
@ConditionalOnProperty(prefix = "iot.transports.tcp", name = "enabled", havingValue = "true")
public class TcpTransportAutoConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(NettyTcpTransportProvider.class)
    NettyTcpTransportProvider tcpTransportProvider(TcpTransportProperties properties) {
        return new NettyTcpTransportProvider(properties);
    }

    @Bean
    ProviderHealthIndicator tcpTransportHealth(NettyTcpTransportProvider transport) {
        return new ProbeProviderHealthIndicator("transport", "tcp", Duration.ofSeconds(5), Duration.ofSeconds(1),
            () -> transport.isRunning()
                ? ProbeProviderHealthIndicator.up(Map.of("snapshot", transport.snapshot()))
                : new org.ssssssss.magicapi.iot.plugin.api.PluginHealth(
                    org.ssssssss.magicapi.iot.plugin.api.PluginHealth.Status.DOWN,
                    "TCP transport is not listening", Map.of()));
    }
}
