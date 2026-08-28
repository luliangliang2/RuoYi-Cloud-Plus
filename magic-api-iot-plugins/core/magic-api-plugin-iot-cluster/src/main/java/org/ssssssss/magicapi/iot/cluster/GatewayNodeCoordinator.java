package org.ssssssss.magicapi.iot.cluster;

import org.springframework.context.SmartLifecycle;
import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class GatewayNodeCoordinator implements SmartLifecycle {
    private final NodeRegistry registry;
    private final GatewayClusterProperties properties;
    private final AtomicBoolean running = new AtomicBoolean();
    private final AtomicLong heartbeatErrors = new AtomicLong();
    private volatile ScheduledExecutorService scheduler;
    private volatile Instant lastHeartbeat;

    public GatewayNodeCoordinator(NodeRegistry registry, GatewayClusterProperties properties) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.properties = Objects.requireNonNull(properties, "properties");
    }

    @Override
    public synchronized void start() {
        if (!running.compareAndSet(false, true)) return;
        validate();
        try {
            registerNow();
            scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "gateway-node-heartbeat");
                thread.setDaemon(true);
                return thread;
            });
            scheduler.scheduleWithFixedDelay(this::heartbeatSafely, properties.getHeartbeatInterval().toMillis(),
                properties.getHeartbeatInterval().toMillis(), TimeUnit.MILLISECONDS);
        } catch (RuntimeException exception) {
            running.set(false);
            throw exception;
        }
    }

    @Override
    public synchronized void stop() {
        if (!running.compareAndSet(true, false)) return;
        ScheduledExecutorService current = scheduler;
        scheduler = null;
        if (current != null) current.shutdownNow();
        registry.remove(properties.getNodeId());
    }

    @Override public boolean isRunning() { return running.get(); }
    @Override public boolean isAutoStartup() { return true; }
    @Override public int getPhase() { return Integer.MAX_VALUE - 200; }

    public Collection<NodeRegistry.GatewayNode> activeNodes() {
        return registry.activeNodes(Instant.now());
    }

    public GatewayClusterSnapshot snapshot() {
        int count;
        try { count = activeNodes().size(); } catch (RuntimeException exception) { count = 0; }
        return new GatewayClusterSnapshot(properties.getNodeId(), registry.providerId(), isRunning(), count,
            lastHeartbeat, heartbeatErrors.get());
    }

    private void registerNow() {
        NodeRegistry.GatewayNode node = node();
        registry.register(node);
        lastHeartbeat = node.lastHeartbeat();
    }

    private void heartbeatSafely() {
        if (!running.get()) return;
        try {
            NodeRegistry.GatewayNode node = registry.heartbeat(node());
            lastHeartbeat = node.lastHeartbeat();
        } catch (RuntimeException exception) {
            heartbeatErrors.incrementAndGet();
        }
    }

    private NodeRegistry.GatewayNode node() {
        java.util.Map<String, String> metadata = new java.util.LinkedHashMap<>(properties.getMetadata());
        metadata.put("clusterPort", String.valueOf(properties.getCommunicationPort()));
        metadata.put("clusterAddress", properties.getCommunicationAdvertiseAddress() == null
            || properties.getCommunicationAdvertiseAddress().isBlank()
            ? properties.getAddress().replaceFirst("(:\\d+)$", ":" + properties.getCommunicationPort())
            : properties.getCommunicationAdvertiseAddress());
        return new NodeRegistry.GatewayNode(properties.getNodeId(), properties.getAddress(), Instant.now(),
            properties.getCapacity(), metadata);
    }

    private void validate() {
        if (properties.getNodeId() == null || properties.getNodeId().isBlank())
            throw new IllegalStateException("iot.cluster.node-id must not be blank");
        if (properties.getAddress() == null || properties.getAddress().isBlank())
            throw new IllegalStateException("iot.cluster.address must not be blank");
        if (properties.getHeartbeatInterval() == null || properties.getHeartbeatInterval().isZero()
            || properties.getHeartbeatInterval().isNegative())
            throw new IllegalStateException("iot.cluster.heartbeat-interval must be positive");
        if (properties.getCommunicationBindAddress() == null || properties.getCommunicationBindAddress().isBlank())
            throw new IllegalStateException("iot.cluster.communication-bind-address must not be blank");
        if (properties.getCommunicationPort() < 1 || properties.getCommunicationPort() > 65535)
            throw new IllegalStateException("iot.cluster.communication-port must be between 1 and 65535");
    }
}
