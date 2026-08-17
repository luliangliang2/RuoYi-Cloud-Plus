package org.ssssssss.magicapi.iot.timeseries;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import java.time.Instant;
import java.util.Map;

public record TimeSeriesPoint(DeviceIdentity device, String measurement, Instant timestamp,
                              Map<String, Object> fields, Map<String, String> tags) {
    public TimeSeriesPoint {
        fields = Map.copyOf(fields);
        tags = tags == null ? Map.of() : Map.copyOf(tags);
    }
}

