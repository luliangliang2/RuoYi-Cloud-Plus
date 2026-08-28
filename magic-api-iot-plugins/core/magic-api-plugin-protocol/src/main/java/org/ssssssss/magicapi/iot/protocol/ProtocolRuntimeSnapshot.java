package org.ssssssss.magicapi.iot.protocol;

import java.util.Set;

public record ProtocolRuntimeSnapshot(
    boolean running,
    Set<String> protocolIds,
    int transportCount,
    long activeConnections,
    long receivedFrames,
    long publishedMessages,
    long unsupportedFrames,
    long errors
) {
}
