package org.ssssssss.magicapi.iot.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class RedisDeviceRegistryIntegrationTest {
    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redis;

    @BeforeEach void connect() {
        connectionFactory = new LettuceConnectionFactory(System.getProperty("iot.redis.host", "10.211.55.4"), Integer.getInteger("iot.redis.port", 6379));
        connectionFactory.afterPropertiesSet();
        redis = new StringRedisTemplate(connectionFactory);
        redis.afterPropertiesSet();
    }

    @AfterEach void close() { connectionFactory.destroy(); }

    @Test void persistsAndAuthenticatesAcrossRegistryInstances() {
        DeviceIdentity id = new DeviceIdentity("iot-test", UUID.randomUUID().toString());
        DeviceCredential credential = new DeviceCredential("secret", "integration-value");
        RedisDeviceRegistry first = new RedisDeviceRegistry(redis, new ObjectMapper().findAndRegisterModules());
        RedisDeviceRegistry second = new RedisDeviceRegistry(redis, new ObjectMapper().findAndRegisterModules());
        try {
            first.save(new RegisteredDevice(id, true, "integration", Map.of("temperature", "number"), 1));
            first.setCredential(id, credential);
            assertEquals("integration", second.find(id).orElseThrow().protocolId());
            assertTrue(second.authenticate(id, credential));
            assertFalse(second.authenticate(id, new DeviceCredential("secret", "wrong")));
        } finally {
            redis.delete("iot:device:" + id.routingKey());
            redis.delete("iot:device:" + id.routingKey() + ":credential:" + credential.type());
        }
    }
}
