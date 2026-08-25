package org.ssssssss.magicapi.iot.shadow;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import java.util.Optional;

public interface DeviceShadowRepository {
    Optional<DeviceShadow> find(DeviceIdentity device);
    DeviceShadow save(DeviceShadow shadow, long expectedVersion);
}

