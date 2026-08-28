package org.ssssssss.magicapi.iot.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.ProbeProviderHealthIndicator;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;

@AutoConfiguration
@ConditionalOnProperty(prefix = "iot.providers.device-registry", name = "type", havingValue = "redis")
public class RedisDeviceRegistryAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean(DeviceRegistry.class)
	RedisDeviceRegistry deviceRegistry(StringRedisTemplate redis, ObjectMapper mapper) {
		return new RedisDeviceRegistry(redis, mapper);
	}

	@Bean
	RedisDeviceIndexMaintenance redisDeviceIndexMaintenance(StringRedisTemplate redis, ObjectMapper mapper) {
		return new RedisDeviceIndexMaintenance(redis, mapper);
	}

	@Bean
	ProviderHealthIndicator redisDeviceRegistryHealth(StringRedisTemplate redis,
			org.springframework.core.env.Environment environment) {
		return new ProbeProviderHealthIndicator("device-registry", "redis", cacheTtl(environment),
			timeout(environment), () -> {
				try (var connection = redis.getConnectionFactory().getConnection()) {
					String pong = connection.ping();
					if (!"PONG".equalsIgnoreCase(pong)) throw new IllegalStateException("Unexpected Redis PING response");
					return ProbeProviderHealthIndicator.up(Map.of("command", "PING", "response", pong));
				}
			});
	}

	private static Duration cacheTtl(org.springframework.core.env.Environment environment) {
		return environment.getProperty("iot.health.cache-ttl", Duration.class, Duration.ofSeconds(10));
	}

	private static Duration timeout(org.springframework.core.env.Environment environment) {
		return environment.getProperty("iot.health.timeout", Duration.class, Duration.ofSeconds(3));
	}
}
