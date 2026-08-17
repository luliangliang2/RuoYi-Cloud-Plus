package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.DeviceSession;

import java.util.Collection;
import java.util.Optional;

public interface SessionRepository {

    DeviceSession register(DeviceSession session);

    Optional<DeviceSession> find(DeviceIdentity device);

    void touch(String sessionId);

    void remove(String sessionId);

    Collection<DeviceSession> findByGatewayNode(String gatewayNodeId);
}

