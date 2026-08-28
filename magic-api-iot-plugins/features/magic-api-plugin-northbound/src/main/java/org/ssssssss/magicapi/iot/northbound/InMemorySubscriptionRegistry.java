package org.ssssssss.magicapi.iot.northbound;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySubscriptionRegistry implements SubscriptionRegistry {
    private final ConcurrentHashMap<String, Subscription> subscriptions = new ConcurrentHashMap<>();
    public Subscription save(Subscription subscription) { subscriptions.put(subscription.subscriptionId(), subscription); return subscription; }
    public boolean remove(String subscriptionId) { return subscriptions.remove(subscriptionId) != null; }
    public Collection<Subscription> find(String topic) {
        return subscriptions.values().stream().filter(s -> s.topics().contains("*") || s.topics().contains(topic)).toList();
    }
}
