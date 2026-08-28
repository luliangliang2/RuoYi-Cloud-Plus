package org.ssssssss.magicapi.iot.handshake.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceHandshakeProvider;

import java.nio.ByteBuffer;
import java.util.Set;

public final class JsonDeviceHandshakeProvider implements DeviceHandshakeProvider {
	private final ObjectMapper mapper;

	public JsonDeviceHandshakeProvider(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public String serviceId() {
		return "json-device-v1";
	}

	@Override
	public String ownerPluginId() {
		return "handshake-basic";
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
		try {
			byte[] bytes = new byte[payload.remaining()];
			payload.asReadOnlyBuffer().get(bytes);
			JsonNode root = mapper.readTree(bytes);
			String productId = required(root, "productId");
			String deviceId = required(root, "deviceId");
			String credential = required(root, "credential");
			String type = root.path("credentialType").asText("secret");
			String authenticator = root.path("authenticator").asText("registry-device");
			return HandshakeResult.authenticate(new DeviceIdentity(productId, deviceId),
					new DeviceCredential(type, credential), authenticator);
		} catch (Exception exception) {
			return HandshakeResult.rejected("Invalid JSON handshake: " + exception.getMessage());
		}
	}

	private static String required(JsonNode root, String field) {
		String value = root.path(field).asText("");
		if (value.isBlank())
			throw new IllegalArgumentException(field + " is required");
		return value;
	}
}
