package org.ssssssss.magicapi.iot.cluster.etcd;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class EtcdNodeRegistry implements NodeRegistry, AutoCloseable {
    private final Client client;
    private final ObjectMapper mapper;
    private final EtcdNodeRegistryProperties properties;
    private final Map<String, Long> leases = new ConcurrentHashMap<>();

    public EtcdNodeRegistry(Client client, ObjectMapper mapper, EtcdNodeRegistryProperties properties) {
        this.client = client;
        this.mapper = mapper;
        this.properties = properties;
    }

    @Override public String providerId() { return "etcd"; }

    @Override
    public GatewayNode register(GatewayNode node) {
        try {
            long leaseId = lease(node.nodeId());
            client.getKVClient().put(key(node.nodeId()), ByteSequence.from(mapper.writeValueAsBytes(node)),
                PutOption.builder().withLeaseId(leaseId).build())
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return node;
        } catch (Exception exception) {
            leases.remove(node.nodeId());
            throw new IllegalStateException("Failed to register gateway node in etcd: " + node.nodeId(), exception);
        }
    }

    @Override
    public Collection<GatewayNode> activeNodes(Instant now) {
        try {
            var response = client.getKVClient().get(prefix(), GetOption.builder().isPrefix(true).build())
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            Instant cutoff = now.minus(properties.getStaleAfter());
            return response.getKvs().stream().map(value -> read(value.getValue().getBytes()))
                .filter(node -> node != null && !node.lastHeartbeat().isBefore(cutoff))
                .toList();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to discover gateway nodes from etcd", exception);
        }
    }

    @Override
    public boolean remove(String nodeId) {
        try {
            long deleted = client.getKVClient().delete(key(nodeId))
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS).getDeleted();
            Long leaseId = leases.remove(nodeId);
            if (leaseId != null) client.getLeaseClient().revoke(leaseId)
                .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
            return deleted > 0;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to remove gateway node from etcd: " + nodeId, exception);
        }
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
        leases.values().forEach(lease -> {
            try { client.getLeaseClient().revoke(lease).get(1, TimeUnit.SECONDS); } catch (Exception ignored) { }
        });
        leases.clear();
        client.close();
    }

    ByteSequence key(String nodeId) {
        String encoded = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(nodeId.getBytes(StandardCharsets.UTF_8));
        return bytes(normalizedPrefix() + encoded);
    }

    private long lease(String nodeId) throws Exception {
        Long current = leases.get(nodeId);
        if (current != null) {
            try {
                client.getLeaseClient().keepAliveOnce(current)
                    .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS);
                return current;
            } catch (Exception exception) {
                leases.remove(nodeId, current);
            }
        }
        long granted = client.getLeaseClient().grant(properties.getLeaseTtl().toSeconds())
            .get(properties.getRequestTimeout().toMillis(), TimeUnit.MILLISECONDS).getID();
        leases.put(nodeId, granted);
        return granted;
    }

    private GatewayNode read(byte[] value) {
        try { return mapper.readValue(value, GatewayNode.class); } catch (Exception exception) { return null; }
    }

    private ByteSequence prefix() { return bytes(normalizedPrefix()); }
    private String normalizedPrefix() { return properties.getRootPrefix().endsWith("/")
        ? properties.getRootPrefix() : properties.getRootPrefix() + "/"; }
    private static ByteSequence bytes(String value) { return ByteSequence.from(value, StandardCharsets.UTF_8); }
}
