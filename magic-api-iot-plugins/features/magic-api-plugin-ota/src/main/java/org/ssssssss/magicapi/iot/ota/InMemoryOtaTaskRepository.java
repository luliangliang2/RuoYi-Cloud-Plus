package org.ssssssss.magicapi.iot.ota;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOtaTaskRepository implements OtaTaskRepository {
    private final ConcurrentHashMap<String, OtaTask> tasks = new ConcurrentHashMap<>();
    public OtaTask save(OtaTask task) { tasks.put(task.taskId(), task); return task; }
    public Optional<OtaTask> find(String taskId) { return Optional.ofNullable(tasks.get(taskId)); }
}

