package org.ssssssss.magicapi.iot.core.spi;

import java.util.List;
import java.util.Optional;

public interface HandshakeProviderRegistry {

    Optional<DeviceHandshakeProvider> handshake(String providerId);

    Optional<DeviceAuthenticator> authenticator(String providerId);

    List<DeviceHandshakeProvider> handshakes();

    List<DeviceAuthenticator> authenticators();
}
