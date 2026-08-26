package org.ssssssss.magicapi.iot.storage;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageRouterTest {
    @Test void routesSingleAndBatchWrites() {
        List<String> written = new ArrayList<>();
        StorageWriterProvider<String> provider = provider("timeseries", written);
        StorageRouter<String> router = new StorageRouter<>(List.of(provider));

        router.route("timeseries", "one");
        router.routeBatch("timeseries", List.of("two", "three"));

        assertEquals(List.of("one", "two", "three"), written);
    }

    @Test void rejectsDuplicateProviderIds() {
        assertThrows(IllegalArgumentException.class, () -> new StorageRouter<>(List.of(
            provider("same", new ArrayList<>()), provider("same", new ArrayList<>()))));
    }

    private StorageWriterProvider<String> provider(String id, List<String> written) {
        return new StorageWriterProvider<>() {
            public String providerId() { return id; }
            public boolean supports(String value) { return value != null; }
            public void write(String value) { written.add(value); }
        };
    }
}
