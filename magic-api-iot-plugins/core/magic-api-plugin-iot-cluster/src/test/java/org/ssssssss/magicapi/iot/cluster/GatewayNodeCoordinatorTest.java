package org.ssssssss.magicapi.iot.cluster;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GatewayNodeCoordinatorTest {
    @Test
    void registersHeartbeatsAndRemovesTheLocalNode() throws Exception {
        RecordingRegistry registry = new RecordingRegistry();
        GatewayClusterProperties properties = new GatewayClusterProperties();
        properties.setNodeId("gateway-a");
        properties.setAddress("10.0.0.1:9218");
        properties.setHeartbeatInterval(Duration.ofMillis(10));
        properties.setMetadata(Map.of("zone", "a"));
        GatewayNodeCoordinator coordinator = new GatewayNodeCoordinator(registry, properties);

        coordinator.start();
        try {
            assertTrue(coordinator.isRunning());
            assertEquals(1, registry.registers.get());
            awaitHeartbeat(registry);
            assertEquals(1, coordinator.activeNodes().size());
            assertNotNull(coordinator.snapshot().lastHeartbeat());
            assertEquals("test", coordinator.snapshot().providerId());
        } finally {
            coordinator.stop();
        }

        assertFalse(coordinator.isRunning());
        assertEquals("gateway-a", registry.removedNodeId);
        assertTrue(registry.nodes.isEmpty());
    }

    private static void awaitHeartbeat(RecordingRegistry registry) throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(1).toNanos();
        while (registry.heartbeats.get() == 0 && System.nanoTime() < deadline) Thread.sleep(5);
        assertTrue(registry.heartbeats.get() > 0);
    }

    private static final class RecordingRegistry implements NodeRegistry {
        private final Map<String, GatewayNode> nodes = new ConcurrentHashMap<>();
        private final AtomicInteger registers = new AtomicInteger();
        private final AtomicInteger heartbeats = new AtomicInteger();
        private volatile String removedNodeId;

        @Override public String providerId() { return "test"; }
        @Override public GatewayNode register(GatewayNode node) {
            registers.incrementAndGet();
            nodes.put(node.nodeId(), node);
            return node;
        }
        @Override public GatewayNode heartbeat(GatewayNode node) {
            heartbeats.incrementAndGet();
            nodes.put(node.nodeId(), node);
            return node;
        }
        @Override public Collection<GatewayNode> activeNodes(Instant now) { return nodes.values(); }
        @Override public boolean remove(String nodeId) {
            removedNodeId = nodeId;
            return nodes.remove(nodeId) != null;
        }
        @Override public boolean isAvailable() { return true; }
    }
}
