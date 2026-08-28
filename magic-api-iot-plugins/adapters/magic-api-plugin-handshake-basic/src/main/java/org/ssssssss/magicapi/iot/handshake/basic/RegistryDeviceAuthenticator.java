package org.ssssssss.magicapi.iot.handshake.basic;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceAuthenticator;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;

public final class RegistryDeviceAuthenticator implements DeviceAuthenticator {
	private final DeviceRegistry registry;

	public RegistryDeviceAuthenticator(DeviceRegistry registry) {
		this.registry = registry;
	}

	@Override
	public String serviceId() {
		return "registry-device";
	}

	@Override
	public String ownerPluginId() {
		return "handshake-basic";
	}

	@Override
	public AuthenticationResult authenticate(DeviceIdentity identity, DeviceCredential credential,
			AuthenticationContext context) {
		var device = registry.find(identity);
		if (device.isEmpty())
			return new AuthenticationResult(false, "", "Device is not registered");
		if (!device.filter(RegisteredDevice::enabled).isPresent())
			return new AuthenticationResult(false, "", "Device is disabled");
		boolean authenticated = registry.authenticate(identity, credential);
		return authenticated ? new AuthenticationResult(true, identity.productId() + "/" + identity.deviceId(), "")
				: new AuthenticationResult(false, "", "Device credential is invalid");
	}
}
