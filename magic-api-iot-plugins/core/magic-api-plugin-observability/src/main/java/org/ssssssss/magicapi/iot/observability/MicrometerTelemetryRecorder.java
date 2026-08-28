package org.ssssssss.magicapi.iot.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.ssssssss.magicapi.iot.core.spi.TelemetryRecorder;

import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;

public final class MicrometerTelemetryRecorder implements TelemetryRecorder {
    private final MeterRegistry registry;

    public MicrometerTelemetryRecorder(MeterRegistry registry) { this.registry = registry; }

    @Override
    public void increment(String metric, Map<String, String> tags) {
        Counter.builder(metric).tags(tagArray(tags)).register(registry).increment();
    }

    @Override
    public void record(String metric, Duration duration, Map<String, String> tags) {
        Timer.builder(metric).tags(tagArray(tags)).register(registry).record(duration);
    }

    @Override
    public long value(String metric, Map<String, String> tags) {
        Counter counter = registry.find(metric).tags(tagArray(tags)).counter();
        return counter == null ? 0L : counter.count() > Long.MAX_VALUE ? Long.MAX_VALUE : (long) counter.count();
    }

    private static String[] tagArray(Map<String, String> tags) {
        Map<String, String> sorted = new TreeMap<>(tags == null ? Map.of() : tags);
        String[] result = new String[sorted.size() * 2];
        int index = 0;
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            result[index++] = entry.getKey();
            result[index++] = entry.getValue() == null ? "" : entry.getValue();
        }
        return result;
    }
}
