package org.ssssssss.magicapi.iot.cluster.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class EtcdNodeRegistryIntegrationTest {
    @Test
    void supportsLeaseHeartbeatCrossInstanceDiscoveryAndRemoval() {
        List<String> endpoints = endpoints();
        String suffix = UUID.randomUUID().toString();
        String nodeId = "gateway-" + suffix;
        EtcdNodeRegistryProperties properties = properties(endpoints, "/iot/integration/nodes/" + suffix + "/");
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        try (EtcdNodeRegistry writer = registry(endpoints, mapper, properties);
             EtcdNodeRegistry reader = registry(endpoints, mapper, properties)) {
            Instant firstHeartbeat = Instant.now();
            NodeRegistry.GatewayNode node = new NodeRegistry.GatewayNode(nodeId, "127.0.0.1:19000",
                firstHeartbeat, 100, Map.of("role", "integration"));
            writer.register(node);

            var discovered = reader.activeNodes(Instant.now()).stream()
                .filter(candidate -> candidate.nodeId().equals(nodeId)).findFirst().orElseThrow();
            assertEquals("integration", discovered.metadata().get("role"));

            writer.heartbeat(node);
            var heartbeat = reader.activeNodes(Instant.now()).stream()
                .filter(candidate -> candidate.nodeId().equals(nodeId)).findFirst().orElseThrow();
            assertTrue(heartbeat.lastHeartbeat().isAfter(firstHeartbeat));
            assertTrue(writer.remove(nodeId));
            assertFalse(reader.activeNodes(Instant.now()).stream()
                .anyMatch(candidate -> candidate.nodeId().equals(nodeId)));
        }
    }

    @Test
    void revokesLeaseWhenOwningRegistryCloses() {
        List<String> endpoints = endpoints();
        String suffix = UUID.randomUUID().toString();
        String nodeId = "gateway-close-" + suffix;
        EtcdNodeRegistryProperties properties = properties(endpoints, "/iot/integration/nodes/" + suffix + "/");
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        EtcdNodeRegistry owner = registry(endpoints, mapper, properties);
        try (EtcdNodeRegistry observer = registry(endpoints, mapper, properties)) {
            owner.register(new NodeRegistry.GatewayNode(nodeId, "127.0.0.1:19001", Instant.now(), 100));
            assertTrue(observer.activeNodes(Instant.now()).stream()
                .anyMatch(candidate -> candidate.nodeId().equals(nodeId)));
            owner.close();
            assertFalse(observer.activeNodes(Instant.now()).stream()
                .anyMatch(candidate -> candidate.nodeId().equals(nodeId)));
        } finally {
            owner.close();
        }
    }

    private static EtcdNodeRegistry registry(List<String> endpoints, ObjectMapper mapper,
                                             EtcdNodeRegistryProperties properties) {
        return new EtcdNodeRegistry(Client.builder().endpoints(endpoints.toArray(String[]::new)).build(),
            mapper, properties);
    }

    private static EtcdNodeRegistryProperties properties(List<String> endpoints, String rootPrefix) {
        EtcdNodeRegistryProperties properties = new EtcdNodeRegistryProperties();
        properties.setEndpoints(endpoints);
        properties.setRootPrefix(rootPrefix);
        properties.setLeaseTtl(Duration.ofSeconds(10));
        properties.setRequestTimeout(Duration.ofSeconds(5));
        properties.setStaleAfter(Duration.ofSeconds(30));
        return properties;
    }

    private static List<String> endpoints() {
        return Arrays.stream(System.getProperty("iot.etcd.endpoints",
                "http://10.211.55.4:2379,http://10.211.55.4:22379,http://10.211.55.4:32379").split(","))
            .map(String::trim).filter(value -> !value.isEmpty()).toList();
    }
}
