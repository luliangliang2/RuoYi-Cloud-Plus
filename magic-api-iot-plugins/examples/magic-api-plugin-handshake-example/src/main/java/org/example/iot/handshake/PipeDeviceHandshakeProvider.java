package org.example.iot.handshake;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public final class PipeDeviceHandshakeProvider implements DeviceHandshakeProvider {
	@Override
	public String serviceId() {
		return "vendor-pipe-v1";
	}

	@Override
	public Set<String> supportedProtocols() {
		return Set.of("raw");
	}

	@Override
	public HandshakeResult onConnect(HandshakeContext context) {
		return HandshakeResult.continueWaiting();
	}

	@Override
	public HandshakeResult onMessage(HandshakeContext context, ByteBuffer payload) {
		byte[] bytes = new byte[payload.remaining()];
		payload.asReadOnlyBuffer().get(bytes);
		String[] fields = new String(bytes, StandardCharsets.UTF_8).trim().split("\\|", -1);
		if (fields.length != 4 || !"HELLO".equals(fields[0]))
			return HandshakeResult.rejected("Expected HELLO|productId|deviceId|credential");
		return HandshakeResult.authenticate(new DeviceIdentity(fields[1], fields[2]),
				new DeviceCredential("secret", fields[3]), "registry-device");
	}
}
