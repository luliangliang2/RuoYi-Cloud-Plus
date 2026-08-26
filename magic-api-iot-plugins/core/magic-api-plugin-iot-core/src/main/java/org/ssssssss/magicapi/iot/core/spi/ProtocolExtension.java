package org.ssssssss.magicapi.iot.core.spi;

public interface ProtocolExtension {
    String protocolId();

    default int priority() {
        return 0;
    }
}
