package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.plugin.api.PluginService;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

public interface DeviceHandshakeProvider extends PluginService {

    Set<String> supportedProtocols();

    HandshakeResult onConnect(HandshakeContext context);

    HandshakeResult onMessage(HandshakeContext context, ByteBuffer payload);

    record HandshakeContext(String connectionId, String protocolId, String transportId,
                            String remoteAddress, Instant connectedAt, int attempt,
                            Map<String, String> attributes) {
        public HandshakeContext {
            connectedAt = connectedAt == null ? Instant.now() : connectedAt;
            attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        }
    }

    record HandshakeResult(Status status, DeviceIdentity identity, DeviceCredential credential,
                           String authenticatorId, byte[] response, String reason,
                           Map<String, String> attributes) {
        public HandshakeResult {
            response = response == null ? new byte[0] : response.clone();
            reason = reason == null ? "" : reason;
            attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        }

        @Override public byte[] response() { return response.clone(); }

        public static HandshakeResult continueWaiting() {
            return new HandshakeResult(Status.CONTINUE, null, null, "", null, "", Map.of());
        }

        public static HandshakeResult authenticate(DeviceIdentity identity, DeviceCredential credential,
                                                   String authenticatorId) {
            return new HandshakeResult(Status.AUTHENTICATE, identity, credential, authenticatorId,
                null, "", Map.of());
        }

        public static HandshakeResult rejected(String reason) {
            return new HandshakeResult(Status.REJECTED, null, null, "", null, reason, Map.of());
        }
    }

    enum Status {
        CONTINUE,
        RESPOND,
        AUTHENTICATE,
        AUTHENTICATED,
        REJECTED,
        CLOSE
    }
}
