package org.ssssssss.magicapi.iot.session;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-session", name = "type", havingValue = "memory")
public class DeviceSessionMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SessionRepository.class)
    SessionRepository sessionRepository() {
        return new InMemorySessionRepository();
    }
}
