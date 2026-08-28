package org.ssssssss.magicapi.iot.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@AutoConfiguration
@ConditionalOnExpression("'${iot.providers.device-registry.type:}' == 'redis' || '${iot.providers.device-session.type:}' == 'redis'")
public class RedisProviderAutoConfiguration {
    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    LettuceConnectionFactory iotRedisConnectionFactory(Environment environment) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(
            environment.getProperty("spring.data.redis.host", "127.0.0.1"),
            environment.getProperty("spring.data.redis.port", Integer.class, 6379));
        configuration.setDatabase(environment.getProperty("spring.data.redis.database", Integer.class, 0));
        String username = environment.getProperty("spring.data.redis.username");
        if (username != null && !username.isBlank()) configuration.setUsername(username);
        String password = environment.getProperty("spring.data.redis.password");
        if (password != null && !password.isBlank()) configuration.setPassword(RedisPassword.of(password));
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    StringRedisTemplate iotStringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
