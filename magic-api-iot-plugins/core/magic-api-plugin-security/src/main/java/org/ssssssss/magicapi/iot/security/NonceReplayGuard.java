package org.ssssssss.magicapi.iot.security;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class NonceReplayGuard {
    private final Duration ttl;
    private final ConcurrentHashMap<String, Instant> seen = new ConcurrentHashMap<>();
    public NonceReplayGuard(Duration ttl) { this.ttl = ttl; }
    public boolean accept(String deviceKey, String nonce) {
        String key = deviceKey + ":" + nonce;
        Instant now = Instant.now();
        seen.entrySet().removeIf(e -> e.getValue().plus(ttl).isBefore(now));
        return seen.putIfAbsent(key, now) == null;
    }
}

