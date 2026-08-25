package org.ssssssss.magicapi.iot.storage;

public interface StorageWriter<T> {
    String storageId();
    void write(T value);
}

