package org.ssssssss.magicapi.iot.handshake.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonDeviceHandshakeProviderTest {
    @Test void parsesIdentityAndCredential() {
        var provider = new JsonDeviceHandshakeProvider(new ObjectMapper());
        var context = new DeviceHandshakeProvider.HandshakeContext("c1", "raw", "tcp", "127.0.0.1",
            Instant.now(), 1, Map.of());
        var result = provider.onMessage(context, ByteBuffer.wrap(
            "{\"productId\":\"robot\",\"deviceId\":\"agv-1\",\"credential\":\"secret\"}"
                .getBytes(StandardCharsets.UTF_8)));
        assertEquals(DeviceHandshakeProvider.Status.AUTHENTICATE, result.status());
        assertEquals("robot", result.identity().productId());
        assertEquals("registry-device", result.authenticatorId());
    }
}
