package org.ssssssss.magicapi.iot.protocol;

import org.springframework.context.SmartLifecycle;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.TransportProvider;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class ProtocolIngressRuntime implements SmartLifecycle, TransportProvider.TransportMessageHandler {
    private final ProtocolPipelineRegistry pipelines;
    private final DeviceMessageBus messageBus;
    private final List<TransportProvider> transports;
    private final AtomicBoolean running = new AtomicBoolean();
    private final AtomicLong activeConnections = new AtomicLong();
    private final AtomicLong receivedFrames = new AtomicLong();
    private final AtomicLong publishedMessages = new AtomicLong();
    private final AtomicLong unsupportedFrames = new AtomicLong();
    private final AtomicLong errors = new AtomicLong();

    public ProtocolIngressRuntime(ProtocolPipelineRegistry pipelines, DeviceMessageBus messageBus,
                                  List<TransportProvider> transports) {
        this.pipelines = Objects.requireNonNull(pipelines, "pipelines");
        this.messageBus = Objects.requireNonNull(messageBus, "messageBus");
        this.transports = List.copyOf(transports);
    }

    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) return;
        try {
            transports.forEach(transport -> transport.start(this));
        } catch (RuntimeException exception) {
            running.set(false);
            transports.forEach(this::closeQuietly);
            throw exception;
        }
    }

    @Override
    public void stop() {
        if (!running.compareAndSet(true, false)) return;
        transports.forEach(this::closeQuietly);
    }

    @Override public boolean isRunning() { return running.get(); }
    @Override public boolean isAutoStartup() { return true; }
    @Override public int getPhase() { return Integer.MAX_VALUE - 100; }

    @Override
    public void connected(String connectionId, ProtocolContext context) {
        activeConnections.incrementAndGet();
    }

    @Override
    public void received(String connectionId, ByteBuffer payload, ProtocolContext context) {
        receivedFrames.incrementAndGet();
        try {
            var pipeline = pipelines.detect(payload.asReadOnlyBuffer(), context);
            if (pipeline.isEmpty()) {
                unsupportedFrames.incrementAndGet();
                return;
            }
            pipeline.orElseThrow().decode(payload.asReadOnlyBuffer(), context).forEach(message -> {
                messageBus.publish(message);
                publishedMessages.incrementAndGet();
            });
        } catch (RuntimeException exception) {
            errors.incrementAndGet();
        }
    }

    @Override
    public void disconnected(String connectionId, ProtocolContext context, Throwable cause) {
        activeConnections.updateAndGet(current -> Math.max(0, current - 1));
        if (cause != null) errors.incrementAndGet();
    }

    public ProtocolRuntimeSnapshot snapshot() {
        return new ProtocolRuntimeSnapshot(isRunning(), pipelines.protocolIds(), transports.size(),
            activeConnections.get(), receivedFrames.get(), publishedMessages.get(),
            unsupportedFrames.get(), errors.get());
    }

    private void closeQuietly(TransportProvider transport) {
        try { transport.close(); } catch (Exception exception) { errors.incrementAndGet(); }
    }
}
