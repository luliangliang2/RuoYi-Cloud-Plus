package org.ssssssss.magicapi.iot.command;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.CommandGateway;

@AutoConfiguration
public class CommandAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CommandGateway.class)
    CommandGateway commandGateway() {
        return new InMemoryCommandGateway();
    }
}

