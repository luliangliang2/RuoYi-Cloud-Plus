package org.ssssssss.magicapi.iot.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-registry", name = "type", havingValue = "redis")
public class RedisDeviceRegistryAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean(DeviceRegistry.class)
	DeviceRegistry deviceRegistry(StringRedisTemplate redis, ObjectMapper mapper) {
		return new RedisDeviceRegistry(redis, mapper);
	}
}
