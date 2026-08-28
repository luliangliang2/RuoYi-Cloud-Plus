package org.ssssssss.magicapi.iot.core.spi;

public record TransportSnapshot(
    String transportId,
    boolean running,
    String bindAddress,
    int port,
    long activeConnections,
    long acceptedConnections,
    long receivedMessages,
    long receivedBytes,
    long sentMessages,
    long sentBytes,
    long errors
) {
}
