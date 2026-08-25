package org.ssssssss.magicapi.iot.timeseries;

import java.util.Collection;
import java.util.List;

public class TimeSeriesRouter {
    private final List<TimeSeriesWriter> writers;
    public TimeSeriesRouter(List<TimeSeriesWriter> writers) { this.writers = List.copyOf(writers); }
    public void write(String storage, Collection<TimeSeriesPoint> points) {
        TimeSeriesWriter writer = writers.stream().filter(it -> it.supports(storage)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No time-series writer for " + storage));
        writer.write(points);
    }
}

