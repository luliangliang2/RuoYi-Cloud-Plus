package org.ssssssss.magicapi.iot.storage;

import java.util.List;

public interface StorageWriterProvider<T> {
    String providerId();

    boolean supports(T value);

    void write(T value);

    default void writeBatch(List<T> values) {
        values.forEach(this::write);
    }
}
