package org.ssssssss.magicapi.iot.plugin.runtime;

import java.time.Instant;

public record PluginServiceSnapshot(
    String pluginId,
    String serviceId,
    String serviceType,
    String implementation,
    String source,
    long invocations,
    long successes,
    long failures,
    Instant registeredAt,
    Instant lastInvokedAt,
    String lastError
) { }
