package org.ssssssss.magicapi.iot.bus;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.message-bus", name = "type", havingValue = "memory")
public class MessageBusMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DeviceMessageBus.class)
    DeviceMessageBus deviceMessageBus() {
        return new InMemoryDeviceMessageBus();
    }

    @Bean
    ProviderHealthIndicator memoryMessageBusHealth() {
        return new ProviderHealthIndicator() {
            public String providerId() { return "memory"; }
            public String providerType() { return "message-bus"; }
            public PluginHealth health() { return PluginHealth.up(); }
        };
    }
}
