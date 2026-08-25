package org.ssssssss.magicapi.iot.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-session", name = "type", havingValue = "redis")
public class RedisSessionAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean(SessionRepository.class)
	SessionRepository sessionRepository(StringRedisTemplate redis, ObjectMapper mapper) {
		return new RedisSessionRepository(redis, mapper);
	}
}
