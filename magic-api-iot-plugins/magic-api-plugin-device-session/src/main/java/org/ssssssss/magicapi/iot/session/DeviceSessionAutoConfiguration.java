package org.ssssssss.magicapi.iot.session;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;

@AutoConfiguration
public class DeviceSessionAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(SessionRepository.class)
    SessionRepository sessionRepository() {
        return new InMemorySessionRepository();
    }
}

