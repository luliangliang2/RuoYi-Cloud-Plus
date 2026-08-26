package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;

public interface DeviceAuthenticator {
	AuthenticationResult authenticate(DeviceIdentity identity, DeviceCredential credential, String nonce);

	record AuthenticationResult(boolean authenticated, String principal, String reason) {
	}
}
