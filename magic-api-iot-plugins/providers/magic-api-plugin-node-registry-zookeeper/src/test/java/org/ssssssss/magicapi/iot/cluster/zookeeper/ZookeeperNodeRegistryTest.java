package org.ssssssss.magicapi.iot.cluster.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ZookeeperNodeRegistryTest {
    @Test
    void registersDiscoversHeartbeatsAndRemovesEphemeralNode() throws Exception {
        try (TestingServer server = new TestingServer()) {
            var client = CuratorFrameworkFactory.newClient(server.getConnectString(), new RetryOneTime(50));
            client.start();
            assertTrue(client.blockUntilConnected(5, java.util.concurrent.TimeUnit.SECONDS));
            var properties = new ZookeeperNodeRegistryProperties();
            properties.setConnectString(server.getConnectString());
            var mapper = new ObjectMapper().findAndRegisterModules();
            try (var registry = new ZookeeperNodeRegistry(client, mapper, properties)) {
                var node = new NodeRegistry.GatewayNode("node/one", "127.0.0.1:9218", Instant.now(), 100,
                    Map.of("zone", "A"));
                registry.register(node);
                assertEquals(1, registry.activeNodes(Instant.now()).size());
                assertEquals("A", registry.activeNodes(Instant.now()).iterator().next().metadata().get("zone"));
                registry.heartbeat(node);
                assertTrue(registry.remove(node.nodeId()));
                assertTrue(registry.activeNodes(Instant.now()).isEmpty());
            }
        }
    }
}
