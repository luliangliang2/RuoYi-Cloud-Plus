package org.ssssssss.magicapi.iot.core.spi;

public interface BackpressurePolicy {
    Decision accept(String stream, int pending);
    enum Decision { ACCEPT, DROP, PAUSE, DEAD_LETTER }
}

