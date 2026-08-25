package org.ssssssss.magicapi.iot.observability;

import org.ssssssss.magicapi.iot.core.spi.TelemetryRecorder;

import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class InMemoryTelemetryRecorder implements TelemetryRecorder {

    private final ConcurrentHashMap<String, LongAdder> values = new ConcurrentHashMap<>();

    @Override
    public void increment(String metric, Map<String, String> tags) {
        values.computeIfAbsent(key(metric, tags), ignored -> new LongAdder()).increment();
    }

    @Override
    public void record(String metric, Duration duration, Map<String, String> tags) {
        values.computeIfAbsent(key(metric, tags), ignored -> new LongAdder()).add(duration.toNanos());
    }

    @Override
    public long value(String metric, Map<String, String> tags) {
        LongAdder value = values.get(key(metric, tags));
        return value == null ? 0 : value.sum();
    }

    private static String key(String metric, Map<String, String> tags) {
        return metric + new TreeMap<>(tags == null ? Map.of() : tags);
    }
}

