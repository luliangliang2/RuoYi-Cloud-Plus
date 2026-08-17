package org.ssssssss.magicapi.iot.storage;

import java.util.List;

public class StorageRouter<T> {
    private final List<StorageWriter<T>> writers;
    public StorageRouter(List<StorageWriter<T>> writers) { this.writers = List.copyOf(writers); }
    public void route(String storageId, T value) {
        writers.stream().filter(w -> w.storageId().equals(storageId)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No writer for storage: " + storageId)).write(value);
    }
}

