package org.ssssssss.magicapi.iot.config;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryConfigurationRuntimeTest {
    @Test
    void mirrorsRemoteChangesAndReparsesRegisteredNamespaces() {
        Center center = new Center();
        try (InMemoryConfigurationRuntime first = new InMemoryConfigurationRuntime(center);
             InMemoryConfigurationRuntime second = new InMemoryConfigurationRuntime(center)) {
            first.registerParser(new ConfigurationParser<String>() {
                @Override public String id() { return "provider"; }
                @Override public String prefix() { return "providers/"; }
                @Override public String parse(Map<String, ConfigurationCenter.ConfigurationValue> values) {
                    return values.getOrDefault("providers/kafka", new ConfigurationCenter.ConfigurationValue("providers/kafka", "", "test:0")).value();
                }
            });
            second.registerParser(new ConfigurationParser<String>() {
                @Override public String id() { return "provider"; }
                @Override public String prefix() { return "providers/"; }
                @Override public String parse(Map<String, ConfigurationCenter.ConfigurationValue> values) {
                    return values.getOrDefault("providers/kafka", new ConfigurationCenter.ConfigurationValue("providers/kafka", "", "test:0")).value();
                }
            });
            center.put("providers/kafka", "enabled");
            assertEquals("enabled", first.parsedSnapshots().get("provider"));
            assertEquals("enabled", second.parsedSnapshots().get("provider"));
            center.put("providers/kafka", "paused");
            assertEquals("paused", first.get("providers/kafka").orElseThrow().value());
            assertEquals("paused", second.parsedSnapshots().get("provider"));
        }
    }

    private static final class Center implements ConfigurationCenter {
        private final Map<String, ConfigurationValue> values = new LinkedHashMap<>();
        private final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<>();
        private final AtomicLong revision = new AtomicLong();
        @Override public String providerId() { return "test"; }
        @Override public synchronized Optional<ConfigurationValue> get(String key) { return Optional.ofNullable(values.get(key)); }
        @Override public synchronized List<ConfigurationValue> list(String prefix) { return values.values().stream().filter(v -> v.key().startsWith(prefix)).toList(); }
        @Override public synchronized ConfigurationValue put(String key, String value) {
            ConfigurationValue result = new ConfigurationValue(key, value, "test:" + revision.incrementAndGet());
            values.put(key, result); listeners.forEach(listener -> listener.onChange(ConfigurationEvent.put(result))); return result;
        }
        @Override public synchronized CasResult compareAndSet(String key, String expectedRevision, String value) { return CasResult.applied(put(key, value)); }
        @Override public synchronized CasResult delete(String key, String expectedRevision) { values.remove(key); return CasResult.applied(null); }
        @Override public WatchSubscription watch(String prefix, ConfigurationListener listener) { listeners.add(listener); return new WatchSubscription() { public void close() { listeners.remove(listener); } public boolean isClosed() { return !listeners.contains(listener); } }; }
        @Override public boolean isAvailable() { return true; }
    }
}
