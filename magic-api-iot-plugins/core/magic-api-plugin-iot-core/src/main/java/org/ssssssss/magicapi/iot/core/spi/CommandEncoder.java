package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceCommand;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;

import java.nio.ByteBuffer;

public interface CommandEncoder extends ProtocolExtension {
    ByteBuffer encodeCommand(DeviceCommand command, ProtocolContext context);
}
