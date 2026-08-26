package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;

import java.nio.ByteBuffer;

public interface MessageDecoder extends ProtocolExtension {
    DeviceMessage decodeMessage(ByteBuffer frame, ProtocolContext context);
}
