package org.ssssssss.magicapi.iot.core.spi;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public interface NodeRegistry {
    String providerId();
    GatewayNode register(GatewayNode node);
    default GatewayNode heartbeat(GatewayNode node) {
        return register(new GatewayNode(node.nodeId(), node.address(), Instant.now(), node.capacity(), node.metadata()));
    }
    Collection<GatewayNode> activeNodes(Instant now);
    boolean remove(String nodeId);
    boolean isAvailable();

    record GatewayNode(String nodeId, String address, Instant lastHeartbeat, int capacity,
                       Map<String, String> metadata) {
        public GatewayNode {
            Objects.requireNonNull(nodeId, "nodeId");
            Objects.requireNonNull(address, "address");
            lastHeartbeat = lastHeartbeat == null ? Instant.now() : lastHeartbeat;
            if (capacity < 0) throw new IllegalArgumentException("capacity must not be negative");
            metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        }

        public GatewayNode(String nodeId, String address, Instant lastHeartbeat, int capacity) {
            this(nodeId, address, lastHeartbeat, capacity, Map.of());
        }
    }
}
