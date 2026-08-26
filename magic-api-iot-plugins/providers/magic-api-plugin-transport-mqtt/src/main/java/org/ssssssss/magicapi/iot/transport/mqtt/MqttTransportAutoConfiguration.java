package org.ssssssss.magicapi.iot.transport.mqtt;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(MqttTransportProperties.class)
@ConditionalOnProperty(prefix = "iot.transports.mqtt", name = "enabled", havingValue = "true")
public class MqttTransportAutoConfiguration {
    @Bean(destroyMethod = "close")
    MicaMqttTransportProvider mqttTransportProvider(MqttTransportProperties properties, DeviceRegistry registry) {
        return new MicaMqttTransportProvider(properties, registry);
    }

    @Bean
    ProviderHealthIndicator mqttTransportHealth(MicaMqttTransportProvider transport) {
        return new ProbeProviderHealthIndicator("transport", "mqtt", Duration.ofSeconds(5), Duration.ofSeconds(1),
            () -> transport.isRunning()
                ? ProbeProviderHealthIndicator.up(Map.of("snapshot", transport.snapshot()))
                : new org.ssssssss.magicapi.iot.plugin.api.PluginHealth(
                    org.ssssssss.magicapi.iot.plugin.api.PluginHealth.Status.DOWN,
                    "MQTT transport is not listening", Map.of()));
    }
}
