package org.ssssssss.magicapi.iot.storage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StorageRouter<T> {
    private final Map<String, StorageWriterProvider<T>> providers;

    public StorageRouter(Collection<StorageWriterProvider<T>> providers) {
        try {
            this.providers = providers.stream().collect(Collectors.toUnmodifiableMap(
                StorageWriterProvider::providerId, Function.identity()));
        } catch (IllegalStateException exception) {
            throw new IllegalArgumentException("Duplicate storage writer provider", exception);
        }
    }

    public void route(String providerId, T value) {
        StorageWriterProvider<T> provider = required(providerId);
        if (!provider.supports(value)) {
            throw new IllegalArgumentException("Storage provider " + providerId + " does not support this value");
        }
        provider.write(value);
    }

    public void routeBatch(String providerId, List<T> values) {
        StorageWriterProvider<T> provider = required(providerId);
        if (values.stream().anyMatch(value -> !provider.supports(value))) {
            throw new IllegalArgumentException("Storage provider " + providerId + " does not support all values");
        }
        provider.writeBatch(List.copyOf(values));
    }

    private StorageWriterProvider<T> required(String providerId) {
        StorageWriterProvider<T> provider = providers.get(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("No storage writer provider: " + providerId);
        }
        return provider;
    }
}
