package org.ssssssss.magicapi.iot.cluster.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EtcdNodeRegistryTest {
    @Test void normalizesAndEncodesNodeKey() {
        EtcdNodeRegistryProperties properties = new EtcdNodeRegistryProperties();
        properties.setRootPrefix("/robot/gateways");
        try (var registry = new EtcdNodeRegistry(Client.builder().endpoints("http://127.0.0.1:2379").build(),
            new ObjectMapper(), properties)) {
            assertTrue(registry.key("node/one").toString(java.nio.charset.StandardCharsets.UTF_8)
                .startsWith("/robot/gateways/"));
            assertFalse(registry.key("node/one").toString(java.nio.charset.StandardCharsets.UTF_8).endsWith("node/one"));
        }
    }
}
