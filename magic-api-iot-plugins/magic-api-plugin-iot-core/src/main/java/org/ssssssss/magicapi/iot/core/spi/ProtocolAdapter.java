package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceCommand;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;

import java.nio.ByteBuffer;
import java.util.List;

public interface ProtocolAdapter {

    String protocolId();

    boolean supports(ByteBuffer input, ProtocolContext context);

    List<ByteBuffer> frame(ByteBuffer input, ProtocolContext context);

    DeviceMessage decode(ByteBuffer frame, ProtocolContext context);

    ByteBuffer encode(DeviceCommand command, ProtocolContext context);
}

