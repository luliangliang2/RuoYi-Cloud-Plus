package org.ssssssss.magicapi.iot.protocol;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.ProtocolAdapter;

import java.util.List;

@AutoConfiguration
public class ProtocolAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    ProtocolRegistry protocolRegistry(List<ProtocolAdapter> adapters) {
        return new ProtocolRegistry(adapters);
    }
}

