package org.ssssssss.magicapi.iot.timeseries;

import java.util.Collection;

public interface TimeSeriesWriter {
    boolean supports(String storage);
    void write(Collection<TimeSeriesPoint> points);
}

