package org.ssssssss.magicapi.iot.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ssssssss.magicapi.iot.core.model.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class RedisSessionRepositoryIntegrationTest {
    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redis;

    @BeforeEach void connect() {
        connectionFactory = new LettuceConnectionFactory(System.getProperty("iot.redis.host", "10.211.55.4"), Integer.getInteger("iot.redis.port", 6379));
        connectionFactory.afterPropertiesSet();
        redis = new StringRedisTemplate(connectionFactory);
        redis.afterPropertiesSet();
    }

    @AfterEach void close() { connectionFactory.destroy(); }

    @Test void sharesSessionAndNodeIndexesAcrossInstances() {
        String suffix = UUID.randomUUID().toString();
        DeviceIdentity device = new DeviceIdentity("iot-test", suffix);
        DeviceSession session = new DeviceSession("session-" + suffix, device, "node-" + suffix,
            "tcp", "127.0.0.1", Instant.now(), Instant.now(), Map.of("channelId", "channel-1"));
        RedisSessionRepository first = new RedisSessionRepository(redis, new ObjectMapper().findAndRegisterModules());
        RedisSessionRepository second = new RedisSessionRepository(redis, new ObjectMapper().findAndRegisterModules());
        try {
            first.register(session);
            assertEquals(session.sessionId(), second.find(device).orElseThrow().sessionId());
            assertEquals(1, second.findByGatewayNode(session.gatewayNodeId()).size());
            second.touch(session.sessionId());
            assertFalse(first.find(device).orElseThrow().lastSeenAt().isBefore(session.lastSeenAt()));
            second.remove(session.sessionId());
            assertTrue(first.find(device).isEmpty());
        } finally {
            redis.delete("iot:session:device:" + device.routingKey());
            redis.delete("iot:session:id:" + session.sessionId());
            redis.delete("iot:session:node:" + session.gatewayNodeId());
        }
    }
}
