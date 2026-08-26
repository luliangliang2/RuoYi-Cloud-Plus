package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;

import java.nio.ByteBuffer;

public interface TransportProvider extends AutoCloseable {
    String transportId();

    void start(TransportMessageHandler handler);

    void send(String connectionId, ByteBuffer payload);

    void disconnect(String connectionId);

    boolean isRunning();

    @Override
    void close();

    interface TransportMessageHandler {
        void connected(String connectionId, ProtocolContext context);
        void received(String connectionId, ByteBuffer payload, ProtocolContext context);
        default void received(String connectionId, DeviceMessage message, ProtocolContext context) {
            throw new UnsupportedOperationException("Direct device messages are not supported");
        }
        void disconnected(String connectionId, ProtocolContext context, Throwable cause);
    }
}
