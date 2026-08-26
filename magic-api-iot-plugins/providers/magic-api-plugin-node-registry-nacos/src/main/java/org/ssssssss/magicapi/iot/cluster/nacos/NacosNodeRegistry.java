package org.ssssssss.magicapi.iot.cluster.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class NacosNodeRegistry implements NodeRegistry, AutoCloseable {
    private static final String NODE_ID = "iot.nodeId";
    private static final String ADDRESS = "iot.address";
    private static final String HEARTBEAT = "iot.lastHeartbeat";
    private static final String CAPACITY = "iot.capacity";
    private final NamingService naming;
    private final NacosNodeRegistryProperties properties;

    public NacosNodeRegistry(NamingService naming, NacosNodeRegistryProperties properties) {
        this.naming = naming;
        this.properties = properties;
    }

    @Override public String providerId() { return "nacos"; }

    @Override
    public GatewayNode register(GatewayNode node) {
        HostPort endpoint = HostPort.parse(node.address());
        Instance instance = new Instance();
        instance.setIp(endpoint.host());
        instance.setPort(endpoint.port());
        instance.setClusterName(properties.getClusterName());
        instance.setEphemeral(true);
        instance.setEnabled(true);
        instance.setHealthy(true);
        Map<String, String> metadata = new LinkedHashMap<>(node.metadata());
        metadata.put(NODE_ID, node.nodeId());
        metadata.put(ADDRESS, node.address());
        metadata.put(HEARTBEAT, node.lastHeartbeat().toString());
        metadata.put(CAPACITY, Integer.toString(node.capacity()));
        instance.setMetadata(metadata);
        try {
            naming.registerInstance(properties.getServiceName(), properties.getGroup(), instance);
            return node;
        } catch (NacosException exception) {
            throw new IllegalStateException("Failed to register gateway node in Nacos: " + node.nodeId(), exception);
        }
    }

    @Override
    public Collection<GatewayNode> activeNodes(Instant now) {
        try {
            Instant cutoff = now.minus(properties.getStaleAfter());
            return naming.selectInstances(properties.getServiceName(), properties.getGroup(), true).stream()
                .map(this::toNode)
                .filter(node -> !node.lastHeartbeat().isBefore(cutoff))
                .toList();
        } catch (NacosException exception) {
            throw new IllegalStateException("Failed to discover gateway nodes from Nacos", exception);
        }
    }

    @Override
    public boolean remove(String nodeId) {
        try {
            boolean removed = false;
            for (Instance instance : naming.getAllInstances(properties.getServiceName(), properties.getGroup())) {
                if (!nodeId.equals(instance.getMetadata().get(NODE_ID))) continue;
                naming.deregisterInstance(properties.getServiceName(), properties.getGroup(), instance.getIp(),
                    instance.getPort(), instance.getClusterName());
                removed = true;
            }
            return removed;
        } catch (NacosException exception) {
            throw new IllegalStateException("Failed to remove gateway node from Nacos: " + nodeId, exception);
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            naming.getServicesOfServer(1, 1, properties.getGroup());
            return true;
        } catch (NacosException exception) {
            return false;
        }
    }

    @Override public void close() {
        try { naming.shutDown(); } catch (NacosException ignored) { }
    }

    private GatewayNode toNode(Instance instance) {
        Map<String, String> metadata = new LinkedHashMap<>(instance.getMetadata());
        String nodeId = metadata.remove(NODE_ID);
        String address = metadata.remove(ADDRESS);
        String heartbeat = metadata.remove(HEARTBEAT);
        String capacity = metadata.remove(CAPACITY);
        return new GatewayNode(nodeId, address == null ? instance.getIp() + ":" + instance.getPort() : address,
            heartbeat == null ? Instant.EPOCH : Instant.parse(heartbeat),
            capacity == null ? 0 : Integer.parseInt(capacity), metadata);
    }

    record HostPort(String host, int port) {
        static HostPort parse(String address) {
            String value = address.contains("://") ? address.substring(address.indexOf("://") + 3) : address;
            int separator = value.lastIndexOf(':');
            if (separator <= 0 || separator == value.length() - 1)
                throw new IllegalArgumentException("Gateway address must be host:port: " + address);
            return new HostPort(value.substring(0, separator), Integer.parseInt(value.substring(separator + 1)));
        }
    }
}
