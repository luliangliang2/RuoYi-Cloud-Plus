package org.ssssssss.magicapi.iot.core.spi;

import java.time.Duration;
import java.util.Map;

public interface TelemetryRecorder {

    void increment(String metric, Map<String, String> tags);

    void record(String metric, Duration duration, Map<String, String> tags);

    long value(String metric, Map<String, String> tags);
}

