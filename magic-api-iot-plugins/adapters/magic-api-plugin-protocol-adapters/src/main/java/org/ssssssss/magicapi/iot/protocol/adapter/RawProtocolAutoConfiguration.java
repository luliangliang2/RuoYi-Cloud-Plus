package org.ssssssss.magicapi.iot.protocol.adapter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RawProtocolAutoConfiguration {
    @Bean
    RawProtocolAdapter rawProtocolAdapter() {
        return new RawProtocolAdapter("raw");
    }
}
