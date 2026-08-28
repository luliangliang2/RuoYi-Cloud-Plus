package org.ssssssss.magicapi.iot.shadow;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDeviceShadowRepository implements DeviceShadowRepository {
    private final ConcurrentHashMap<String, DeviceShadow> shadows = new ConcurrentHashMap<>();

    public Optional<DeviceShadow> find(DeviceIdentity device) {
        return Optional.ofNullable(shadows.get(device.routingKey()));
    }

    public DeviceShadow save(DeviceShadow shadow, long expectedVersion) {
        return shadows.compute(shadow.device().routingKey(), (key, current) -> {
            long currentVersion = current == null ? 0 : current.version();
            if (currentVersion != expectedVersion) throw new IllegalStateException("Device shadow version conflict");
            return new DeviceShadow(shadow.device(), shadow.reported(), shadow.desired(), currentVersion + 1, null);
        });
    }
}

