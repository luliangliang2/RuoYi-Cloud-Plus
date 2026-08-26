package org.ssssssss.magicapi.iot.protocol.modbus;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.protocols.modbus-tcp", name = "enabled", havingValue = "true")
public class ModbusTcpProtocolAutoConfiguration {
    @Bean
    ModbusTcpProtocolAdapter modbusTcpProtocolAdapter() {
        return new ModbusTcpProtocolAdapter();
    }
}
