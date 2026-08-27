package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.plugin.api.PluginService;

public interface DeviceAuthenticator extends PluginService {
	AuthenticationResult authenticate(DeviceIdentity identity, DeviceCredential credential, AuthenticationContext context);

	default AuthenticationResult authenticate(DeviceIdentity identity, DeviceCredential credential, String nonce) {
		return authenticate(identity, credential, new AuthenticationContext("", "", nonce, java.time.Instant.now(), java.util.Map.of()));
	}

	record AuthenticationResult(boolean authenticated, String principal, String reason) {
	}

	record AuthenticationContext(String protocolId, String remoteAddress, String nonce,
								 java.time.Instant receivedAt, java.util.Map<String, String> attributes) {
		public AuthenticationContext {
			receivedAt = receivedAt == null ? java.time.Instant.now() : receivedAt;
			attributes = attributes == null ? java.util.Map.of() : java.util.Map.copyOf(attributes);
		}
	}
}
