package org.ssssssss.magicapi.iot.config;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationCenterContractTest {
    @Test
    void supportsListWatchAndCompareAndSet() {
        MemoryCenter center = new MemoryCenter();
        List<ConfigurationCenter.ConfigurationEvent> events = new ArrayList<>();
        ConfigurationCenter.WatchSubscription watch = center.watch("robot/", events::add);

        var created = center.put("robot/speed", "1.0");
        center.put("other/key", "ignored");
        var rejected = center.compareAndSet("robot/speed", "memory:0", "2.0");
        var updated = center.compareAndSet("robot/speed", created.revision(), "2.0");
        var deleted = center.delete("robot/speed", updated.current().orElseThrow().revision());
        watch.close();

        assertFalse(rejected.applied());
        assertTrue(updated.applied());
        assertTrue(deleted.applied());
        assertTrue(center.list("robot/").isEmpty());
        assertEquals(List.of(ConfigurationCenter.EventType.PUT, ConfigurationCenter.EventType.PUT,
            ConfigurationCenter.EventType.DELETE), events.stream().map(ConfigurationCenter.ConfigurationEvent::type).toList());
        assertTrue(watch.isClosed());
    }

    private static final class MemoryCenter implements ConfigurationCenter {
        private final Map<String, ConfigurationValue> values = new LinkedHashMap<>();
        private final List<Registration> listeners = new ArrayList<>();
        private final AtomicLong revision = new AtomicLong();

        @Override public String providerId() { return "memory"; }
        @Override public Optional<ConfigurationValue> get(String key) { return Optional.ofNullable(values.get(key)); }
        @Override public List<ConfigurationValue> list(String prefix) {
            return values.values().stream().filter(value -> value.key().startsWith(prefix)).toList();
        }
        @Override public ConfigurationValue put(String key, String value) {
            ConfigurationValue current = next(key, value);
            values.put(key, current);
            publish(ConfigurationEvent.put(current));
            return current;
        }
        @Override public CasResult compareAndSet(String key, String expectedRevision, String value) {
            ConfigurationValue current = values.get(key);
            if (current == null || !current.revision().equals(expectedRevision)) return CasResult.rejected(Optional.ofNullable(current));
            return CasResult.applied(put(key, value));
        }
        @Override public CasResult delete(String key, String expectedRevision) {
            ConfigurationValue current = values.get(key);
            if (current == null || !current.revision().equals(expectedRevision)) return CasResult.rejected(Optional.ofNullable(current));
            values.remove(key);
            String nextRevision = "memory:" + revision.incrementAndGet();
            publish(ConfigurationEvent.deleted(key, nextRevision));
            return CasResult.applied(null);
        }
        @Override public WatchSubscription watch(String prefix, ConfigurationListener listener) {
            Registration registration = new Registration(prefix, listener);
            listeners.add(registration);
            return registration;
        }
        @Override public boolean isAvailable() { return true; }
        private ConfigurationValue next(String key, String value) {
            return new ConfigurationValue(key, value, "memory:" + revision.incrementAndGet());
        }
        private void publish(ConfigurationEvent event) {
            listeners.stream().filter(listener -> !listener.closed.get() && event.key().startsWith(listener.prefix))
                .forEach(listener -> listener.listener.onChange(event));
        }
        private final class Registration implements WatchSubscription {
            private final String prefix;
            private final ConfigurationListener listener;
            private final AtomicBoolean closed = new AtomicBoolean();
            private Registration(String prefix, ConfigurationListener listener) { this.prefix = prefix; this.listener = listener; }
            @Override public void close() { closed.set(true); listeners.remove(this); }
            @Override public boolean isClosed() { return closed.get(); }
        }
    }
}
