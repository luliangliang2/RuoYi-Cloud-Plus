package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;

import java.util.Optional;

public interface DeviceRegistry {

    Optional<RegisteredDevice> find(DeviceIdentity identity);

    RegisteredDevice save(RegisteredDevice device);

    boolean authenticate(DeviceIdentity identity, DeviceCredential credential);
}

