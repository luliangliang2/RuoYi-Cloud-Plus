package org.ssssssss.magicapi.iot.ota;

import java.util.Optional;

public interface OtaTaskRepository {
    OtaTask save(OtaTask task);
    Optional<OtaTask> find(String taskId);
}

