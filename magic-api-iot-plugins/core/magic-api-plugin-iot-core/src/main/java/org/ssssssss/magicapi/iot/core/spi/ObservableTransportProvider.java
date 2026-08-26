package org.ssssssss.magicapi.iot.core.spi;

public interface ObservableTransportProvider extends TransportProvider {
    TransportSnapshot snapshot();
}
