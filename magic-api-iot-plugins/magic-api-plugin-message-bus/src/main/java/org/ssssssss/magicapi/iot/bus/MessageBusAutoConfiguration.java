package org.ssssssss.magicapi.iot.bus;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;

@AutoConfiguration
public class MessageBusAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(DeviceMessageBus.class)
    DeviceMessageBus deviceMessageBus() {
        return new InMemoryDeviceMessageBus();
    }
}

