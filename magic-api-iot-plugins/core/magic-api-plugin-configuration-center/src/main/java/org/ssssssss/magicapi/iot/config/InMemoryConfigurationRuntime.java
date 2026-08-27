package org.ssssssss.magicapi.iot.config;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Keeps a node-local immutable-by-value view while the selected configuration center is authoritative.
 * Provider watch events are the cross-node synchronization mechanism.
 */
public final class InMemoryConfigurationRuntime implements ConfigurationRuntime {
    private final ConfigurationCenter center;
    private final Map<String, ConfigurationCenter.ConfigurationValue> values = new ConcurrentHashMap<>();
    private final Map<String, Object> parsed = new ConcurrentHashMap<>();
    private final Map<String, String> parserErrors = new ConcurrentHashMap<>();
    private final List<ConfigurationParser<?>> parsers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean();
    private final ConfigurationCenter.WatchSubscription subscription;

    public InMemoryConfigurationRuntime(ConfigurationCenter center) {
        this.center = Objects.requireNonNull(center, "center");
        reload();
        this.subscription = center.watch("", this::apply);
        // Reconcile once after watch registration so an update cannot fall between list and watch.
        reload();
        reparseAll();
    }

    @Override public String providerId() { return center.providerId(); }

    @Override public Optional<ConfigurationCenter.ConfigurationValue> get(String key) {
        return Optional.ofNullable(values.get(key));
    }

    @Override public List<ConfigurationCenter.ConfigurationValue> list(String prefix) {
        return values.values().stream().filter(value -> value.key().startsWith(prefix))
            .sorted(Comparator.comparing(ConfigurationCenter.ConfigurationValue::key)).toList();
    }

    @Override public ConfigurationCenter.ConfigurationValue put(String key, String value) {
        ConfigurationCenter.ConfigurationValue result = center.put(key, value);
        reloadAndReparse();
        return result;
    }

    @Override public ConfigurationCenter.CasResult compareAndSet(String key, String expectedRevision, String value) {
        ConfigurationCenter.CasResult result = center.compareAndSet(key, expectedRevision, value);
        reloadAndReparse();
        return result;
    }

    @Override public ConfigurationCenter.CasResult delete(String key, String expectedRevision) {
        ConfigurationCenter.CasResult result = center.delete(key, expectedRevision);
        reloadAndReparse();
        return result;
    }

    @Override public Map<String, Object> parsedSnapshots() {
        return Map.copyOf(parsed);
    }

    @Override public Map<String, String> parserErrors() { return Map.copyOf(parserErrors); }

    @Override public <T> void registerParser(ConfigurationParser<T> parser) {
        Objects.requireNonNull(parser, "parser");
        if (parsers.stream().anyMatch(existing -> existing.id().equals(parser.id())))
            throw new IllegalArgumentException("Duplicate configuration parser: " + parser.id());
        parsers.add(parser);
        reparse(parser);
    }

    private void reload() {
        Map<String, ConfigurationCenter.ConfigurationValue> latest = center.list("").stream()
            .collect(Collectors.toMap(ConfigurationCenter.ConfigurationValue::key, value -> value));
        values.keySet().retainAll(latest.keySet());
        values.putAll(latest);
    }

    private void reloadAndReparse() {
        reload();
        reparseAll();
    }

    private void apply(ConfigurationCenter.ConfigurationEvent event) {
        if (closed.get()) return;
        if (event.type() == ConfigurationCenter.EventType.PUT) {
            values.put(event.key(), new ConfigurationCenter.ConfigurationValue(
                event.key(), event.value().orElseThrow(), event.revision()));
        } else {
            values.remove(event.key());
        }
        reparseAll();
    }

    private void reparseAll() {
        parsers.forEach(this::reparse);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void reparse(ConfigurationParser parser) {
        Map<String, ConfigurationCenter.ConfigurationValue> snapshot = values.values().stream()
            .filter(value -> value.key().startsWith(parser.prefix()))
            .collect(Collectors.toUnmodifiableMap(ConfigurationCenter.ConfigurationValue::key, value -> value));
        try {
            Object result = Objects.requireNonNull(parser.parse(snapshot), "parsed configuration");
            parser.apply(result);
            parsed.put(parser.id(), Objects.requireNonNull(parser.snapshot(result), "configuration snapshot"));
            parserErrors.remove(parser.id());
        } catch (RuntimeException exception) {
            parserErrors.put(parser.id(), exception.getMessage());
        }
    }

    @Override public void close() {
        if (closed.compareAndSet(false, true)) subscription.close();
    }
}
