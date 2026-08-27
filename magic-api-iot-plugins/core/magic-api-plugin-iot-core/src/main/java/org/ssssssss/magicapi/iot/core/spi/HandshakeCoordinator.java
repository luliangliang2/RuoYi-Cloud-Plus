package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.ProtocolContext;

import java.nio.ByteBuffer;

public interface HandshakeCoordinator {

    void connected(String connectionId, ProtocolContext context);

    Decision received(String connectionId, ByteBuffer payload, ProtocolContext context);

    void disconnected(String connectionId);

    record Decision(boolean forward, boolean close, ByteBuffer response, ProtocolContext context, String reason) {
        public Decision {
            response = response == null ? ByteBuffer.allocate(0) : response.asReadOnlyBuffer();
            reason = reason == null ? "" : reason;
        }

        public static Decision forward(ProtocolContext context) {
            return new Decision(true, false, null, context, "");
        }
    }
}
