package org.ssssssss.magicapi.iot.config.etcd;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.op.Cmp;
import io.etcd.jetcd.op.CmpTarget;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class EtcdConfigurationCenter implements ConfigurationCenter, AutoCloseable {
    private final Client client;
    private final EtcdConfigurationCenterProperties properties;
    private final CopyOnWriteArraySet<EtcdWatch> watches = new CopyOnWriteArraySet<>();

    public EtcdConfigurationCenter(Client client, EtcdConfigurationCenterProperties properties) {
        this.client = Objects.requireNonNull(client, "client");
        this.properties = Objects.requireNonNull(properties, "properties");
    }

    @Override public String providerId() { return "etcd"; }

    @Override
    public Optional<ConfigurationValue> get(String key) {
        requireKey(key);
        try {
            var response = client.getKVClient().get(key(key))
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return response.getKvs().stream().findFirst().map(value -> toValue(key, value));
        } catch (Exception exception) {
            throw failure("read", key, exception);
        }
    }

    @Override
    public List<ConfigurationValue> list(String keyPrefix) {
        Objects.requireNonNull(keyPrefix, "prefix");
        try {
            var response = client.getKVClient().get(prefix(), GetOption.builder().isPrefix(true).build())
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return response.getKvs().stream().map(this::toValue)
                .filter(value -> value.key().startsWith(keyPrefix))
                .sorted(Comparator.comparing(ConfigurationValue::key)).toList();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to list etcd configuration prefix: " + keyPrefix, exception);
        }
    }

    @Override
    public ConfigurationValue put(String key, String value) {
        requireKey(key);
        Objects.requireNonNull(value, "value");
        try {
            var response = client.getKVClient().put(key(key), bytes(value))
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return new ConfigurationValue(key, value, revision(response.getHeader().getRevision()));
        } catch (Exception exception) {
            throw failure("write", key, exception);
        }
    }

    @Override
    public CasResult compareAndSet(String key, String expectedRevision, String value) {
        requireKey(key);
        Objects.requireNonNull(value, "value");
        long expected = parseRevision(expectedRevision);
        try {
            var response = client.getKVClient().txn()
                .If(new Cmp(key(key), Cmp.Op.EQUAL, CmpTarget.modRevision(expected)))
                .Then(Op.put(key(key), bytes(value), io.etcd.jetcd.options.PutOption.DEFAULT))
                .commit().get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            if (!response.isSucceeded()) return CasResult.rejected(get(key));
            return CasResult.applied(new ConfigurationValue(key, value, revision(response.getHeader().getRevision())));
        } catch (Exception exception) {
            throw failure("compare and set", key, exception);
        }
    }

    @Override
    public CasResult delete(String key, String expectedRevision) {
        requireKey(key);
        long expected = parseRevision(expectedRevision);
        try {
            var response = client.getKVClient().txn()
                .If(new Cmp(key(key), Cmp.Op.EQUAL, CmpTarget.modRevision(expected)))
                .Then(Op.delete(key(key), io.etcd.jetcd.options.DeleteOption.DEFAULT))
                .commit().get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            if (!response.isSucceeded()) return CasResult.rejected(get(key));
            return CasResult.applied(null);
        } catch (Exception exception) {
            throw failure("delete", key, exception);
        }
    }

    @Override
    public WatchSubscription watch(String keyPrefix, ConfigurationListener listener) {
        Objects.requireNonNull(keyPrefix, "prefix");
        Objects.requireNonNull(listener, "listener");
        WatchOption option = WatchOption.newBuilder().isPrefix(true).withPrevKV(true).build();
        AtomicBoolean closed = new AtomicBoolean();
        Watch.Watcher watcher = client.getWatchClient().watch(prefix(), option, Watch.listener(response -> {
            if (closed.get()) return;
            response.getEvents().forEach(event -> notify(listener, keyPrefix, event));
        }, ignored -> { }));
        EtcdWatch watch = new EtcdWatch(watcher, closed);
        watches.add(watch);
        return watch;
    }

    @Override
    public boolean isAvailable() {
        try {
            client.getKVClient().get(prefix(), GetOption.builder().withLimit(1).isPrefix(true).build())
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public void close() {
        new ArrayList<>(watches).forEach(EtcdWatch::close);
        client.close();
    }

    private void notify(ConfigurationListener listener, String keyPrefix, WatchEvent event) {
        KeyValue keyValue = event.getKeyValue();
        String key;
        try { key = decodeKey(keyValue.getKey()); } catch (RuntimeException exception) { return; }
        if (!key.startsWith(keyPrefix)) return;
        ConfigurationEvent change = event.getEventType() == WatchEvent.EventType.DELETE
            ? ConfigurationEvent.deleted(key, revision(keyValue.getModRevision()))
            : ConfigurationEvent.put(toValue(key, keyValue));
        try { listener.onChange(change); } catch (RuntimeException ignored) { }
    }

    ByteSequence key(String key) {
        String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(key.getBytes(StandardCharsets.UTF_8));
        return bytes(properties.getRootPrefix() + encoded);
    }
    String decodeKey(ByteSequence key) {
        String encoded = key.toString(StandardCharsets.UTF_8).substring(properties.getRootPrefix().length());
        return new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
    }
    private ByteSequence prefix() { return bytes(properties.getRootPrefix()); }
    private ConfigurationValue toValue(KeyValue keyValue) { return toValue(decodeKey(keyValue.getKey()), keyValue); }
    private static ConfigurationValue toValue(String key, KeyValue keyValue) {
        return new ConfigurationValue(key, keyValue.getValue().toString(StandardCharsets.UTF_8), revision(keyValue.getModRevision()));
    }
    private static ByteSequence bytes(String value) { return ByteSequence.from(value, StandardCharsets.UTF_8); }
    static String revision(long revision) { return "etcd:" + revision; }
    static long parseRevision(String token) {
        if (token == null || !token.startsWith("etcd:"))
            throw new IllegalArgumentException("Expected an etcd revision token");
        try { return Long.parseLong(token.substring("etcd:".length())); }
        catch (NumberFormatException exception) { throw new IllegalArgumentException("Invalid etcd revision token", exception); }
    }
    private static void requireKey(String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("configuration key must not be blank");
    }
    private static IllegalStateException failure(String operation, String key, Exception exception) {
        return new IllegalStateException("Failed to " + operation + " etcd configuration: " + key, exception);
    }

    private final class EtcdWatch implements WatchSubscription {
        private final Watch.Watcher watcher;
        private final AtomicBoolean closed;
        private EtcdWatch(Watch.Watcher watcher, AtomicBoolean closed) { this.watcher = watcher; this.closed = closed; }
        @Override public void close() {
            if (!closed.compareAndSet(false, true)) return;
            watcher.close();
            watches.remove(this);
        }
        @Override public boolean isClosed() { return closed.get(); }
    }
}
