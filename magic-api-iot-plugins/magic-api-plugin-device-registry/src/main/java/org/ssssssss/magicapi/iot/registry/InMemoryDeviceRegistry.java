package org.ssssssss.magicapi.iot.registry;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDeviceRegistry implements DeviceRegistry {

    private final ConcurrentHashMap<String, RegisteredDevice> devices = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> credentialHashes = new ConcurrentHashMap<>();

    @Override
    public Optional<RegisteredDevice> find(DeviceIdentity identity) {
        return Optional.ofNullable(devices.get(identity.routingKey()));
    }

    @Override
    public RegisteredDevice save(RegisteredDevice device) {
        devices.put(device.identity().routingKey(), device);
        return device;
    }

    public void setCredential(DeviceIdentity identity, DeviceCredential credential) {
        credentialHashes.put(identity.routingKey() + ":" + credential.type(), hash(credential.value()));
    }

    @Override
    public boolean authenticate(DeviceIdentity identity, DeviceCredential credential) {
        RegisteredDevice device = devices.get(identity.routingKey());
        String expected = credentialHashes.get(identity.routingKey() + ":" + credential.type());
        return device != null && device.enabled() && expected != null
            && MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), hash(credential.value()).getBytes(StandardCharsets.UTF_8));
    }

    private static String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

