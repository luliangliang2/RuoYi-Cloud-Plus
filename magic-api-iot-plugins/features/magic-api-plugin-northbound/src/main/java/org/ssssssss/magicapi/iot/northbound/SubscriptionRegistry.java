package org.ssssssss.magicapi.iot.northbound;

import java.util.Collection;

public interface SubscriptionRegistry {
    Subscription save(Subscription subscription);
    boolean remove(String subscriptionId);
    Collection<Subscription> find(String topic);
}
