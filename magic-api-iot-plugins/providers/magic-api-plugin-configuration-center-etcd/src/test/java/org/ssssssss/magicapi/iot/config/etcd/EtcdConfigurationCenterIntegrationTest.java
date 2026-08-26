package org.ssssssss.magicapi.iot.config.etcd;

import io.etcd.jetcd.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "iot.integration.enabled", matches = "true")
class EtcdConfigurationCenterIntegrationTest {
    @Test
    void supportsClusterReplicationWatchAndNativeCas() throws Exception {
        List<String> endpoints = endpoints();
        String suffix = UUID.randomUUID().toString();
        String rootPrefix = "/iot/integration/config/" + suffix + "/";
        String key = "robot/fleet/speed";
        EtcdConfigurationCenterProperties properties = properties(endpoints, rootPrefix);

        try (EtcdConfigurationCenter center = new EtcdConfigurationCenter(
            Client.builder().endpoints(endpoints.toArray(String[]::new)).build(), properties)) {
            List<ConfigurationCenter.ConfigurationEvent> events = new CopyOnWriteArrayList<>();
            ConfigurationCenter.WatchSubscription watch = center.watch("robot/", events::add);
            try {
                Thread.sleep(200);
                var created = center.put(key, "1.0");
                center.put("ignored/key", "ignored");

                assertEquals("1.0", center.get(key).orElseThrow().value());
                assertEquals(List.of(key), center.list("robot/").stream()
                    .map(ConfigurationCenter.ConfigurationValue::key).toList());
                assertReplicatedToEveryEndpoint(endpoints, rootPrefix, key, "1.0");
                long createdRevision = EtcdConfigurationCenter.parseRevision(created.revision());
                assertFalse(center.compareAndSet(key, "etcd:" + Math.max(0, createdRevision - 1), "stale").applied());

                var updated = center.compareAndSet(key, created.revision(), "2.0");
                assertTrue(updated.applied());
                assertReplicatedToEveryEndpoint(endpoints, rootPrefix, key, "2.0");
                assertTrue(center.delete(key, updated.current().orElseThrow().revision()).applied());
                awaitEvents(events, 3);
                assertEquals(List.of(ConfigurationCenter.EventType.PUT, ConfigurationCenter.EventType.PUT,
                    ConfigurationCenter.EventType.DELETE), events.stream()
                    .map(ConfigurationCenter.ConfigurationEvent::type).toList());
            } finally {
                watch.close();
                center.get(key).ifPresent(value -> center.delete(key, value.revision()));
                center.get("ignored/key").ifPresent(value -> center.delete("ignored/key", value.revision()));
            }
        }
    }

    private static void assertReplicatedToEveryEndpoint(List<String> endpoints, String rootPrefix,
                                                         String key, String expected) {
        for (String endpoint : endpoints) {
            try (EtcdConfigurationCenter replica = new EtcdConfigurationCenter(
                Client.builder().endpoints(endpoint).build(), properties(List.of(endpoint), rootPrefix))) {
                assertEquals(expected, replica.get(key).orElseThrow().value(),
                    "Configuration was not replicated to " + endpoint);
            }
        }
    }

    private static EtcdConfigurationCenterProperties properties(List<String> endpoints, String rootPrefix) {
        EtcdConfigurationCenterProperties properties = new EtcdConfigurationCenterProperties();
        properties.setEndpoints(endpoints);
        properties.setRootPrefix(rootPrefix);
        properties.setRequestTimeout(Duration.ofSeconds(5));
        return properties;
    }

    private static List<String> endpoints() {
        return Arrays.stream(System.getProperty("iot.etcd.endpoints",
                "http://10.211.55.4:2379,http://10.211.55.4:22379,http://10.211.55.4:32379").split(","))
            .map(String::trim).filter(value -> !value.isEmpty()).toList();
    }

    private static void awaitEvents(List<?> events, int expected) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (events.size() < expected && System.nanoTime() < deadline) Thread.sleep(20);
        assertEquals(expected, events.size());
    }
}
