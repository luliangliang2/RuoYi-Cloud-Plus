package org.ssssssss.magicapi.iot.transport.mqtt.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@EnableConfigurationProperties(MqttClientTransportProperties.class)
@ConditionalOnProperty(prefix = "iot.transports.mqtt-client", name = "enabled", havingValue = "true")
public class MqttClientTransportAutoConfiguration {
    @Bean(destroyMethod = "close")
    PahoMqttClientTransportProvider mqttClientTransportProvider(MqttClientTransportProperties properties,
                                                                 DeviceRegistry registry) {
        return new PahoMqttClientTransportProvider(properties, registry);
    }

    @Bean
    ProviderHealthIndicator mqttClientTransportHealth(PahoMqttClientTransportProvider transport) {
        return new ProbeProviderHealthIndicator("transport", "mqtt-client", Duration.ofSeconds(5), Duration.ofSeconds(1),
            () -> transport.isRunning()
                ? ProbeProviderHealthIndicator.up(Map.of("snapshot", transport.snapshot()))
                : new PluginHealth(PluginHealth.Status.DOWN, "MQTT broker client is disconnected", Map.of(
                    "snapshot", transport.snapshot())));
    }
}
