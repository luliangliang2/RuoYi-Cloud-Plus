package org.ssssssss.magicapi.iot.cluster;

import java.time.Instant;

public record GatewayClusterSnapshot(String nodeId, String providerId, boolean running, int activeNodes,
		Instant lastHeartbeat, long heartbeatErrors) {
}
