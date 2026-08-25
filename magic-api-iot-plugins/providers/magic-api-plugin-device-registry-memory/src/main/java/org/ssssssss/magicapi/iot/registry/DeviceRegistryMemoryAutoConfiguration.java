package org.ssssssss.magicapi.iot.registry;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-registry", name = "type", havingValue = "memory")
public class DeviceRegistryMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DeviceRegistry.class)
    DeviceRegistry deviceRegistry() {
        return new InMemoryDeviceRegistry();
    }
}
