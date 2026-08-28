package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import java.time.Instant;
import java.util.Optional;

public interface DeviceRouteRepository {
    Optional<DeviceRoute> find(DeviceIdentity device);
    DeviceRoute upsert(DeviceRoute route);
    boolean remove(DeviceIdentity device, String expectedSessionId);
    record DeviceRoute(DeviceIdentity device, String nodeId, String channelId, Instant lastHeartbeat, long version) {}
}

