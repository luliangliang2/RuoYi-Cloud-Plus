package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.plugin.api.PluginHealth;
import org.ssssssss.magicapi.iot.plugin.api.ProviderHealthIndicator;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public final class ProbeProviderHealthIndicator implements ProviderHealthIndicator {
    private static final ExecutorService PROBE_EXECUTOR = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable, "iot-provider-health");
        thread.setDaemon(true);
        return thread;
    });

    private final String providerType;
    private final String providerId;
    private final Duration cacheTtl;
    private final Duration timeout;
    private final Probe probe;
    private final AtomicReference<CompletableFuture<PluginHealth>> inFlight = new AtomicReference<>();
    private volatile CachedHealth cached = new CachedHealth(PluginHealth.Status.UNKNOWN, null, 0);

    public ProbeProviderHealthIndicator(String providerType, String providerId, Duration cacheTtl,
                                        Duration timeout, Probe probe) {
        this.providerType = Objects.requireNonNull(providerType, "providerType");
        this.providerId = Objects.requireNonNull(providerId, "providerId");
        this.cacheTtl = Objects.requireNonNull(cacheTtl, "cacheTtl");
        this.timeout = Objects.requireNonNull(timeout, "timeout");
        this.probe = Objects.requireNonNull(probe, "probe");
    }

    @Override public String providerId() { return providerId; }
    @Override public String providerType() { return providerType; }

    @Override
    public PluginHealth health() {
        long now = System.nanoTime();
        CachedHealth current = cached;
        if (current.health != null && now < current.expiresAtNanos) return current.health;

        CompletableFuture<PluginHealth> future = inFlight.get();
        if (future == null || future.isDone()) {
            CompletableFuture<PluginHealth> created = CompletableFuture.supplyAsync(this::runProbe, PROBE_EXECUTOR)
                .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (inFlight.compareAndSet(future, created)) future = created;
            else future = inFlight.get();
        }
        try {
            PluginHealth health = future.get(timeout.toMillis() + 100, TimeUnit.MILLISECONDS);
            cached = new CachedHealth(health.status(), health, System.nanoTime() + cacheTtl.toNanos());
            return health;
        } catch (Exception exception) {
            Throwable cause = exception instanceof ExecutionException && exception.getCause() != null
                ? exception.getCause() : exception;
            PluginHealth health = new PluginHealth(PluginHealth.Status.DOWN,
                cause instanceof TimeoutException ? "Health probe timed out" : safeMessage(cause),
                Map.of("timeoutMs", timeout.toMillis()));
            cached = new CachedHealth(health.status(), health, System.nanoTime() + cacheTtl.toNanos());
            return health;
        }
    }

    private PluginHealth runProbe() {
        long started = System.nanoTime();
        try {
            Map<String, Object> details = new java.util.HashMap<>(probe.check());
            details.put("latencyMs", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started));
            return new PluginHealth(PluginHealth.Status.UP, "Provider is reachable", details);
        } catch (Exception exception) {
            throw new CompletionException(exception);
        }
    }

    private static String safeMessage(Throwable cause) {
        String message = cause.getMessage();
        return message == null || message.isBlank() ? cause.getClass().getSimpleName() : message;
    }

    @FunctionalInterface
    public interface Probe {
        Map<String, Object> check() throws Exception;
    }

    private record CachedHealth(PluginHealth.Status status, PluginHealth health, long expiresAtNanos) {
    }
}
