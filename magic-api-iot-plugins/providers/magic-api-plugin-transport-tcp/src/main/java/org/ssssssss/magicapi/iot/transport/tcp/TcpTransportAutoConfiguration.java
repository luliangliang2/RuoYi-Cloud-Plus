package org.ssssssss.magicapi.iot.transport.tcp;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration
public class TcpTransportAutoConfiguration {
    @Bean
    @ConfigurationProperties("iot.transports.tcp")
    TcpTransportProperties tcpTransportProperties() {
        return new TcpTransportProperties();
    }

    @Bean
    @ConfigurationProperties("iot.transports.modbus-tcp")
    ModbusTcpTransportProperties modbusTcpTransportProperties() {
        return new ModbusTcpTransportProperties();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "iot.transports.tcp", name = "enabled", havingValue = "true")
    NettyTcpTransportProvider tcpTransportProvider(
        @org.springframework.beans.factory.annotation.Qualifier("tcpTransportProperties") TcpTransportProperties properties) {
        return new NettyTcpTransportProvider(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "iot.transports.tcp", name = "enabled", havingValue = "true")
    ProviderHealthIndicator tcpTransportHealth(
        @org.springframework.beans.factory.annotation.Qualifier("tcpTransportProvider") NettyTcpTransportProvider transport) {
        return new ProbeProviderHealthIndicator("transport", "tcp", Duration.ofSeconds(5), Duration.ofSeconds(1),
            () -> transport.isRunning()
                ? ProbeProviderHealthIndicator.up(Map.of("snapshot", transport.snapshot()))
                : new org.ssssssss.magicapi.iot.plugin.api.PluginHealth(
                    org.ssssssss.magicapi.iot.plugin.api.PluginHealth.Status.DOWN,
                    "TCP transport is not listening", Map.of()));
    }

    @Bean(name = "modbusTcpTransportProvider", destroyMethod = "close")
    @ConditionalOnProperty(prefix = "iot.transports.modbus-tcp", name = "enabled", havingValue = "true")
    NettyTcpTransportProvider modbusTcpTransportProvider(
        @org.springframework.beans.factory.annotation.Qualifier("modbusTcpTransportProperties") ModbusTcpTransportProperties properties) {
        return new NettyTcpTransportProvider(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "iot.transports.modbus-tcp", name = "enabled", havingValue = "true")
    ProviderHealthIndicator modbusTcpTransportHealth(
        @org.springframework.beans.factory.annotation.Qualifier("modbusTcpTransportProvider") NettyTcpTransportProvider transport) {
        return new ProbeProviderHealthIndicator("transport", "modbus-tcp", Duration.ofSeconds(5), Duration.ofSeconds(1),
            () -> transport.isRunning()
                ? ProbeProviderHealthIndicator.up(Map.of("snapshot", transport.snapshot()))
                : new org.ssssssss.magicapi.iot.plugin.api.PluginHealth(
                    org.ssssssss.magicapi.iot.plugin.api.PluginHealth.Status.DOWN,
                    "Modbus TCP transport is not listening", Map.of()));
    }
}
