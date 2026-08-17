package org.ssssssss.magicapi.iot.northbound;

import java.util.Set;

public record Subscription(String subscriptionId, String tenantId, Set<String> topics, String callbackUrl) {
    public Subscription {
        topics = topics == null ? Set.of() : Set.copyOf(topics);
    }
}

