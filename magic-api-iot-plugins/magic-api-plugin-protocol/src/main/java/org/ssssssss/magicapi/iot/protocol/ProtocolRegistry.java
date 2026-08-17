package org.ssssssss.magicapi.iot.protocol;

import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.ProtocolAdapter;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProtocolRegistry {

    private final Map<String, ProtocolAdapter> adapters = new ConcurrentHashMap<>();

    public ProtocolRegistry(Collection<ProtocolAdapter> initialAdapters) {
        initialAdapters.forEach(this::register);
    }

    public void register(ProtocolAdapter adapter) {
        ProtocolAdapter previous = adapters.putIfAbsent(adapter.protocolId(), adapter);
        if (previous != null) throw new IllegalArgumentException("Duplicate protocol: " + adapter.protocolId());
    }

    public Optional<ProtocolAdapter> find(String protocolId) {
        return Optional.ofNullable(adapters.get(protocolId));
    }

    public Optional<ProtocolAdapter> detect(ByteBuffer input, ProtocolContext context) {
        return adapters.values().stream().filter(it -> it.supports(input.asReadOnlyBuffer(), context)).findFirst();
    }

    public Collection<String> protocolIds() {
        return Set.copyOf(adapters.keySet());
    }
}

