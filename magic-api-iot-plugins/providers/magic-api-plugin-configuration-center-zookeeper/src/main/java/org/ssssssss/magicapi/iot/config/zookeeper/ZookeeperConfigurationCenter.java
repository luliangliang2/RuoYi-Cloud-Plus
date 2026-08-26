package org.ssssssss.magicapi.iot.config.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ZookeeperConfigurationCenter implements ConfigurationCenter, AutoCloseable {
    private final CuratorFramework client;
    private final ZookeeperConfigurationCenterProperties properties;
    private final CopyOnWriteArraySet<ZookeeperWatch> watches = new CopyOnWriteArraySet<>();

    public ZookeeperConfigurationCenter(CuratorFramework client, ZookeeperConfigurationCenterProperties properties) {
        this.client = Objects.requireNonNull(client, "client");
        this.properties = Objects.requireNonNull(properties, "properties");
        ensureRoot();
    }

    @Override public String providerId() { return "zookeeper"; }

    @Override
    public Optional<ConfigurationValue> get(String key) {
        requireKey(key);
        try {
            Stat stat = new Stat();
            byte[] data = client.getData().storingStatIn(stat).forPath(path(key));
            return Optional.of(value(key, data, stat));
        } catch (KeeperException.NoNodeException exception) {
            return Optional.empty();
        } catch (Exception exception) {
            throw failure("read", key, exception);
        }
    }

    @Override
    public List<ConfigurationValue> list(String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        try {
            if (client.checkExists().forPath(properties.getRootPath()) == null) return List.of();
            List<ConfigurationValue> values = new ArrayList<>();
            for (String child : client.getChildren().forPath(properties.getRootPath())) {
                String key = decode(child);
                if (!key.startsWith(prefix)) continue;
                get(key).ifPresent(values::add);
            }
            values.sort(Comparator.comparing(ConfigurationValue::key));
            return List.copyOf(values);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to list ZooKeeper configuration prefix: " + prefix, exception);
        }
    }

    @Override
    public ConfigurationValue put(String key, String content) {
        requireKey(key);
        Objects.requireNonNull(content, "value");
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        try {
            Stat stat;
            try {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path(key), data);
                stat = client.checkExists().forPath(path(key));
            } catch (KeeperException.NodeExistsException exception) {
                stat = client.setData().forPath(path(key), data);
            }
            return value(key, data, stat);
        } catch (Exception exception) {
            throw failure("write", key, exception);
        }
    }

    @Override
    public CasResult compareAndSet(String key, String expectedRevision, String content) {
        requireKey(key);
        Objects.requireNonNull(content, "value");
        Revision expected = Revision.parse(expectedRevision);
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        try {
            Stat stat = client.setData().withVersion(expected.version()).forPath(path(key), data);
            return CasResult.applied(value(key, data, stat));
        } catch (KeeperException.BadVersionException | KeeperException.NoNodeException exception) {
            return CasResult.rejected(get(key));
        } catch (Exception exception) {
            throw failure("compare and set", key, exception);
        }
    }

    @Override
    public CasResult delete(String key, String expectedRevision) {
        requireKey(key);
        Revision expected = Revision.parse(expectedRevision);
        try {
            client.delete().withVersion(expected.version()).forPath(path(key));
            return CasResult.applied(null);
        } catch (KeeperException.BadVersionException | KeeperException.NoNodeException exception) {
            return CasResult.rejected(get(key));
        } catch (Exception exception) {
            throw failure("delete", key, exception);
        }
    }

    @Override
    public WatchSubscription watch(String prefix, ConfigurationListener listener) {
        Objects.requireNonNull(prefix, "prefix");
        Objects.requireNonNull(listener, "listener");
        ZookeeperWatch watch = new ZookeeperWatch(prefix, listener);
        watches.add(watch);
        watch.start();
        return watch;
    }

    @Override public boolean isAvailable() { return client.getZookeeperClient().isConnected(); }

    @Override
    public void close() {
        new ArrayList<>(watches).forEach(ZookeeperWatch::close);
        client.close();
    }

    private void ensureRoot() {
        try {
            if (client.checkExists().forPath(properties.getRootPath()) == null)
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(properties.getRootPath());
        } catch (KeeperException.NodeExistsException ignored) {
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to initialize ZooKeeper configuration root", exception);
        }
    }

    private String path(String key) { return properties.getRootPath() + "/" + encode(key); }
    private static String encode(String key) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }
    private static String decode(String child) {
        return new String(Base64.getUrlDecoder().decode(child), StandardCharsets.UTF_8);
    }
    private static ConfigurationValue value(String key, byte[] data, Stat stat) {
        return new ConfigurationValue(key, new String(data, StandardCharsets.UTF_8), Revision.of(stat));
    }
    private static void requireKey(String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("configuration key must not be blank");
    }
    private static IllegalStateException failure(String operation, String key, Exception exception) {
        return new IllegalStateException("Failed to " + operation + " ZooKeeper configuration: " + key, exception);
    }

    private record Revision(int version, long zxid) {
        static Revision parse(String token) {
            if (token == null) throw new IllegalArgumentException("Expected a ZooKeeper revision token");
            String[] parts = token.split(":", -1);
            if (parts.length != 3 || !"zookeeper".equals(parts[0]))
                throw new IllegalArgumentException("Expected a ZooKeeper revision token");
            try { return new Revision(Integer.parseInt(parts[1]), Long.parseLong(parts[2])); }
            catch (NumberFormatException exception) { throw new IllegalArgumentException("Invalid ZooKeeper revision token", exception); }
        }
        static String of(Stat stat) { return "zookeeper:" + stat.getVersion() + ":" + stat.getMzxid(); }
    }

    private final class ZookeeperWatch implements CuratorCacheListener, WatchSubscription {
        private final String prefix;
        private final ConfigurationListener listener;
        private final CuratorCache cache = CuratorCache.build(client, properties.getRootPath());
        private final AtomicBoolean closed = new AtomicBoolean();

        private ZookeeperWatch(String prefix, ConfigurationListener listener) {
            this.prefix = prefix;
            this.listener = listener;
            cache.listenable().addListener(this);
        }

        private void start() { cache.start(); }

        @Override
        public void event(Type type, ChildData oldData, ChildData data) {
            if (closed.get()) return;
            ChildData source = type == Type.NODE_DELETED ? oldData : data;
            if (source == null || source.getPath().equals(properties.getRootPath())) return;
            String child = source.getPath().substring(source.getPath().lastIndexOf('/') + 1);
            String key;
            try { key = decode(child); } catch (IllegalArgumentException exception) { return; }
            if (!key.startsWith(prefix)) return;
            ConfigurationEvent event = type == Type.NODE_DELETED
                ? ConfigurationEvent.deleted(key, Revision.of(source.getStat()))
                : ConfigurationEvent.put(value(key, source.getData(), source.getStat()));
            try { listener.onChange(event); } catch (RuntimeException ignored) { }
        }

        @Override
        public void close() {
            if (!closed.compareAndSet(false, true)) return;
            cache.close();
            watches.remove(this);
        }

        @Override public boolean isClosed() { return closed.get(); }
    }
}
