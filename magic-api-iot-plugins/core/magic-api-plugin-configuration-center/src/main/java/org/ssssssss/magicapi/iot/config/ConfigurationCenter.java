package org.ssssssss.magicapi.iot.config;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface ConfigurationCenter {
    String providerId();

    Optional<ConfigurationValue> get(String key);

    List<ConfigurationValue> list(String prefix);

    ConfigurationValue put(String key, String value);

    CasResult compareAndSet(String key, String expectedRevision, String value);

    CasResult delete(String key, String expectedRevision);

    WatchSubscription watch(String prefix, ConfigurationListener listener);

    boolean isAvailable();

    record ConfigurationValue(String key, String value, String revision) {
        public ConfigurationValue {
            requireText(key, "key");
            Objects.requireNonNull(value, "value");
            requireText(revision, "revision");
        }
    }

    record CasResult(boolean applied, Optional<ConfigurationValue> current) {
        public CasResult {
            current = current == null ? Optional.empty() : current;
        }

        public static CasResult applied(ConfigurationValue current) {
            return new CasResult(true, Optional.ofNullable(current));
        }

        public static CasResult rejected(Optional<ConfigurationValue> current) {
            return new CasResult(false, current);
        }
    }

    record ConfigurationEvent(EventType type, String key, Optional<String> value, String revision) {
        public ConfigurationEvent {
            Objects.requireNonNull(type, "type");
            requireText(key, "key");
            value = value == null ? Optional.empty() : value;
            requireText(revision, "revision");
        }

        public static ConfigurationEvent put(ConfigurationValue value) {
            return new ConfigurationEvent(EventType.PUT, value.key(), Optional.of(value.value()), value.revision());
        }

        public static ConfigurationEvent deleted(String key, String revision) {
            return new ConfigurationEvent(EventType.DELETE, key, Optional.empty(), revision);
        }
    }

    enum EventType { PUT, DELETE }

    @FunctionalInterface
    interface ConfigurationListener {
        void onChange(ConfigurationEvent event);
    }

    interface WatchSubscription extends AutoCloseable {
        @Override void close();
        boolean isClosed();
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
    }
}
