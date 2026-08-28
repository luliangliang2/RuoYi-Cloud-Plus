package org.ssssssss.magicapi.iot.config.etcd;

import io.etcd.jetcd.Client;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EtcdConfigurationCenterTest {
    @Test
    void encodesKeysAndUsesOpaqueRevisionTokens() {
        EtcdConfigurationCenterProperties properties = new EtcdConfigurationCenterProperties();
        try (EtcdConfigurationCenter center = new EtcdConfigurationCenter(Client.builder()
            .endpoints("http://127.0.0.1:2379").build(), properties)) {
            var encoded = center.key("robot/fleet/speed");
            assertEquals("robot/fleet/speed", center.decodeKey(encoded));
            assertEquals("etcd:42", EtcdConfigurationCenter.revision(42));
            assertEquals(42, EtcdConfigurationCenter.parseRevision("etcd:42"));
            assertThrows(IllegalArgumentException.class, () -> EtcdConfigurationCenter.parseRevision("nacos:42"));
            assertEquals(true, encoded.toString(StandardCharsets.UTF_8).startsWith(properties.getRootPrefix()));
        }
    }
}
