package org.ssssssss.magicapi.iot.config.nacos;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class NacosConfigurationCenter implements ConfigurationCenter, AutoCloseable {
    private static final String MISSING_REVISION = "nacos:missing";
    private static final TypeReference<Map<String, String>> MAP_TYPE = new TypeReference<>() { };
    private final ConfigService configService;
    private final ObjectMapper mapper;
    private final NacosConfigurationCenterProperties properties;
    private final CopyOnWriteArraySet<NacosWatch> watches = new CopyOnWriteArraySet<>();

    public NacosConfigurationCenter(ConfigService configService, ObjectMapper mapper,
                                    NacosConfigurationCenterProperties properties) {
        this.configService = Objects.requireNonNull(configService, "configService");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.properties = Objects.requireNonNull(properties, "properties");
    }

    @Override public String providerId() { return "nacos"; }

    @Override
    public Optional<ConfigurationValue> get(String key) {
        requireKey(key);
        Document document = readDocument();
        return Optional.ofNullable(document.values().get(key))
            .map(value -> new ConfigurationValue(key, value, document.revision()));
    }

    @Override
    public List<ConfigurationValue> list(String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        Document document = readDocument();
        return document.values().entrySet().stream().filter(entry -> entry.getKey().startsWith(prefix))
            .map(entry -> new ConfigurationValue(entry.getKey(), entry.getValue(), document.revision())).toList();
    }

    @Override
    public ConfigurationValue put(String key, String value) {
        requireKey(key);
        Objects.requireNonNull(value, "value");
        for (int attempt = 0; attempt < properties.getMaxCasRetries(); attempt++) {
            Document current = readDocument();
            Map<String, String> updated = new TreeMap<>(current.values());
            updated.put(key, value);
            String content = write(updated);
            if (publish(current, content)) return new ConfigurationValue(key, value, revision(content));
        }
        throw new IllegalStateException("Nacos configuration update exceeded CAS retries: " + key);
    }

    @Override
    public CasResult compareAndSet(String key, String expectedRevision, String value) {
        requireKey(key);
        requireNacosRevision(expectedRevision);
        Objects.requireNonNull(value, "value");
        Document current = readDocument();
        if (!current.revision().equals(expectedRevision) || !current.values().containsKey(key))
            return CasResult.rejected(value(current, key));
        Map<String, String> updated = new TreeMap<>(current.values());
        updated.put(key, value);
        String content = write(updated);
        try {
            if (!configService.publishConfigCas(properties.getDataId(), properties.getGroup(), content, md5(current.raw())))
                return CasResult.rejected(get(key));
            return CasResult.applied(new ConfigurationValue(key, value, revision(content)));
        } catch (NacosException exception) {
            throw failure("compare and set", key, exception);
        }
    }

    @Override
    public CasResult delete(String key, String expectedRevision) {
        requireKey(key);
        requireNacosRevision(expectedRevision);
        Document current = readDocument();
        if (!current.revision().equals(expectedRevision) || !current.values().containsKey(key))
            return CasResult.rejected(value(current, key));
        Map<String, String> updated = new TreeMap<>(current.values());
        updated.remove(key);
        String content = write(updated);
        try {
            if (!configService.publishConfigCas(properties.getDataId(), properties.getGroup(), content, md5(current.raw())))
                return CasResult.rejected(get(key));
            return CasResult.applied(null);
        } catch (NacosException exception) {
            throw failure("delete", key, exception);
        }
    }

    @Override
    public WatchSubscription watch(String prefix, ConfigurationListener listener) {
        Objects.requireNonNull(prefix, "prefix");
        Objects.requireNonNull(listener, "listener");
        NacosWatch watch = new NacosWatch(prefix, listener, readDocument());
        try {
            configService.addListener(properties.getDataId(), properties.getGroup(), watch);
            watches.add(watch);
            return watch;
        } catch (NacosException exception) {
            throw new IllegalStateException("Failed to watch Nacos configuration prefix: " + prefix, exception);
        }
    }

    @Override public boolean isAvailable() { return "UP".equalsIgnoreCase(configService.getServerStatus()); }

    @Override
    public void close() {
        new ArrayList<>(watches).forEach(NacosWatch::close);
        try { configService.shutDown(); } catch (NacosException ignored) { }
    }

    private boolean publish(Document current, String content) {
        try {
            if (current.raw() == null)
                return configService.publishConfig(properties.getDataId(), properties.getGroup(), content, "json");
            return configService.publishConfigCas(properties.getDataId(), properties.getGroup(), content, "json", md5(current.raw()));
        } catch (NacosException exception) {
            throw new IllegalStateException("Failed to publish Nacos configuration document", exception);
        }
    }

    private Document readDocument() {
        try {
            return parse(configService.getConfig(properties.getDataId(), properties.getGroup(), properties.getTimeout().toMillis()));
        } catch (NacosException exception) {
            throw new IllegalStateException("Failed to read Nacos configuration document", exception);
        }
    }

    private Document parse(String raw) {
        if (raw == null || raw.isBlank()) return new Document(null, Map.of(), MISSING_REVISION);
        try {
            Map<String, String> values = mapper.readValue(raw, MAP_TYPE);
            return new Document(raw, Map.copyOf(values), revision(raw));
        } catch (Exception exception) {
            throw new IllegalStateException("Invalid Nacos configuration document JSON", exception);
        }
    }

    private String write(Map<String, String> values) {
        try { return mapper.writeValueAsString(new TreeMap<>(values)); }
        catch (Exception exception) { throw new IllegalStateException("Failed to serialize Nacos configuration document", exception); }
    }

    static String revision(String raw) { return "nacos:" + md5(raw); }
    private static String md5(String raw) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("MD5").digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("MD5 is unavailable", exception);
        }
    }

    private static Optional<ConfigurationValue> value(Document document, String key) {
        return Optional.ofNullable(document.values().get(key))
            .map(value -> new ConfigurationValue(key, value, document.revision()));
    }

    private static void requireKey(String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("configuration key must not be blank");
    }

    private static void requireNacosRevision(String revision) {
        if (revision == null || !revision.startsWith("nacos:"))
            throw new IllegalArgumentException("Expected a Nacos revision token");
    }

    private static IllegalStateException failure(String operation, String key, Exception exception) {
        return new IllegalStateException("Failed to " + operation + " Nacos configuration: " + key, exception);
    }

    private record Document(String raw, Map<String, String> values, String revision) { }

    private final class NacosWatch implements Listener, WatchSubscription {
        private final String prefix;
        private final ConfigurationListener listener;
        private final AtomicReference<Document> current;
        private final AtomicBoolean closed = new AtomicBoolean();

        private NacosWatch(String prefix, ConfigurationListener listener, Document current) {
            this.prefix = prefix;
            this.listener = listener;
            this.current = new AtomicReference<>(current);
        }

        @Override public Executor getExecutor() { return null; }

        @Override
        public void receiveConfigInfo(String configInfo) {
            if (closed.get()) return;
            Document previous = current.getAndSet(parse(configInfo));
            Document next = current.get();
            next.values().forEach((key, value) -> {
                if (key.startsWith(prefix) && !Objects.equals(previous.values().get(key), value))
                    notifyListener(ConfigurationEvent.put(new ConfigurationValue(key, value, next.revision())));
            });
            previous.values().keySet().stream()
                .filter(key -> key.startsWith(prefix) && !next.values().containsKey(key))
                .forEach(key -> notifyListener(ConfigurationEvent.deleted(key, next.revision())));
        }

        private void notifyListener(ConfigurationEvent event) {
            try { listener.onChange(event); } catch (RuntimeException ignored) { }
        }

        @Override
        public void close() {
            if (!closed.compareAndSet(false, true)) return;
            configService.removeListener(properties.getDataId(), properties.getGroup(), this);
            watches.remove(this);
        }

        @Override public boolean isClosed() { return closed.get(); }
    }
}
