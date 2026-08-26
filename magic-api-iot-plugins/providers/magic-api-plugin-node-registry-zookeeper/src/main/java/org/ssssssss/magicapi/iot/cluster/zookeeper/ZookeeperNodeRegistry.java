package org.ssssssss.magicapi.iot.cluster.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ZookeeperNodeRegistry implements NodeRegistry, AutoCloseable {
    private final CuratorFramework client;
    private final ObjectMapper mapper;
    private final ZookeeperNodeRegistryProperties properties;
    private final Set<String> localNodes = ConcurrentHashMap.newKeySet();

    public ZookeeperNodeRegistry(CuratorFramework client, ObjectMapper mapper,
                                 ZookeeperNodeRegistryProperties properties) {
        this.client = client;
        this.mapper = mapper;
        this.properties = properties;
    }

    @Override public String providerId() { return "zookeeper"; }

    @Override
    public GatewayNode register(GatewayNode node) {
        String path = path(node.nodeId());
        try {
            byte[] data = mapper.writeValueAsBytes(node);
            if (localNodes.contains(node.nodeId()) && client.checkExists().forPath(path) != null) {
                client.setData().forPath(path, data);
            } else {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data);
                localNodes.add(node.nodeId());
            }
            return node;
        } catch (KeeperException.NodeExistsException exception) {
            throw new IllegalStateException("Gateway node ID is already registered in ZooKeeper: " + node.nodeId(), exception);
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to register gateway node in ZooKeeper: " + node.nodeId(), exception);
        }
    }

    @Override
    public Collection<GatewayNode> activeNodes(Instant now) {
        try {
            if (client.checkExists().forPath(properties.getRootPath()) == null) return java.util.List.of();
            Instant cutoff = now.minus(properties.getStaleAfter());
            return client.getChildren().forPath(properties.getRootPath()).stream()
                .map(this::readNode)
                .filter(node -> node != null && !node.lastHeartbeat().isBefore(cutoff))
                .toList();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to discover gateway nodes from ZooKeeper", exception);
        }
    }

    @Override
    public boolean remove(String nodeId) {
        try {
            String path = path(nodeId);
            if (client.checkExists().forPath(path) == null) return false;
            client.delete().forPath(path);
            localNodes.remove(nodeId);
            return true;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to remove gateway node from ZooKeeper: " + nodeId, exception);
        }
    }

    @Override public boolean isAvailable() { return client.getZookeeperClient().isConnected(); }
    @Override public void close() { client.close(); }

    private GatewayNode readNode(String child) {
        try { return mapper.readValue(client.getData().forPath(properties.getRootPath() + "/" + child), GatewayNode.class); }
        catch (Exception exception) { return null; }
    }

    private String path(String nodeId) {
        String encoded = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(nodeId.getBytes(StandardCharsets.UTF_8));
        return properties.getRootPath() + "/" + encoded;
    }
}
