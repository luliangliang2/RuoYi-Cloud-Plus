package org.ssssssss.magicapi.iot.core.spi;

import java.time.Instant;
import java.util.Collection;

public interface NodeRegistry {
    GatewayNode register(GatewayNode node);
    Collection<GatewayNode> activeNodes(Instant now);
    boolean remove(String nodeId);
    record GatewayNode(String nodeId, String address, Instant lastHeartbeat, int capacity) {}
}

