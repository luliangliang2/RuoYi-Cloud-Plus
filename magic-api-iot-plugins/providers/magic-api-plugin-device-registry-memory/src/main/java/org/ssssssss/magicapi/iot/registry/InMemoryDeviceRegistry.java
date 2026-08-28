package org.ssssssss.magicapi.iot.registry;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistryAdmin;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDeviceRegistry implements DeviceRegistry, DeviceRegistryAdmin {

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

    @Override
    public RegisteredDevice register(RegisteredDevice device) {
        if (devices.putIfAbsent(device.identity().routingKey(), device) != null)
            throw new IllegalArgumentException("Device is already registered: " + device.identity().routingKey());
        return device;
    }

    @Override
    public RegisteredDevice update(RegisteredDevice device) {
        if (devices.replace(device.identity().routingKey(), device) == null)
            throw new IllegalArgumentException("Device is not registered: " + device.identity().routingKey());
        return device;
    }

    @Override
    public void delete(DeviceIdentity identity) {
        if (devices.remove(identity.routingKey()) == null)
            throw new IllegalArgumentException("Device is not registered: " + identity.routingKey());
        String prefix = identity.routingKey() + ":";
        credentialHashes.keySet().removeIf(key -> key.startsWith(prefix));
    }

    @Override
    public void setCredential(DeviceIdentity identity, DeviceCredential credential) {
        credentialHashes.put(identity.routingKey() + ":" + credential.type(), hash(credential.value()));
    }

    @Override
    public DevicePage search(String productId, String keyword, int page, int pageSize) {
        int safePage = Math.max(1, page);
        int safeSize = Math.min(100, Math.max(1, pageSize));
        String product = productId == null ? "" : productId.trim();
        String query = keyword == null ? "" : keyword.trim().toLowerCase();
        var matched = devices.values().stream()
            .filter(device -> product.isEmpty() || device.identity().productId().equals(product))
            .filter(device -> query.isEmpty() || device.identity().deviceId().toLowerCase().contains(query))
            .sorted(java.util.Comparator.comparing(device -> device.identity().routingKey())).toList();
        int from = Math.min(matched.size(), (safePage - 1) * safeSize);
        int to = Math.min(matched.size(), from + safeSize);
        return new DevicePage(matched.subList(from, to), matched.size(), safePage, safeSize);
    }

    @Override
    public RegisteredDevice setEnabled(DeviceIdentity identity, boolean enabled) {
        RegisteredDevice current = find(identity).orElseThrow(() -> new IllegalArgumentException("Device is not registered: " + identity.routingKey()));
        return save(new RegisteredDevice(identity, enabled, current.protocolId(), current.capabilities(), current.version() + 1));
    }

    @Override
    public java.util.Set<String> credentialTypes(DeviceIdentity identity) {
        String prefix = identity.routingKey() + ":";
        return credentialHashes.keySet().stream().filter(key -> key.startsWith(prefix))
            .map(key -> key.substring(prefix.length())).collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    @Override
    public void deleteCredential(DeviceIdentity identity, String credentialType) {
        credentialHashes.remove(identity.routingKey() + ":" + credentialType);
    }

    @Override
    public boolean verifyCredential(DeviceIdentity identity, DeviceCredential credential) {
        return authenticate(identity, credential);
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
