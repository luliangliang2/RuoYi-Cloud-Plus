package org.ssssssss.magicapi.iot.registry;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-registry", name = "type", havingValue = "memory")
public class DeviceRegistryMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DeviceRegistry.class)
    DeviceRegistry deviceRegistry() {
        return new InMemoryDeviceRegistry();
    }

    @Bean
    ProviderHealthIndicator memoryDeviceRegistryHealth() {
        return localHealth("device-registry");
    }

    private static ProviderHealthIndicator localHealth(String type) {
        return new ProviderHealthIndicator() {
            public String providerId() { return "memory"; }
            public String providerType() { return type; }
            public PluginHealth health() { return PluginHealth.up(); }
        };
    }
}
