package org.ssssssss.magicapi.iot.registry;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;

@AutoConfiguration
public class DeviceRegistryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DeviceRegistry.class)
    DeviceRegistry deviceRegistry() {
        return new InMemoryDeviceRegistry();
    }
}

