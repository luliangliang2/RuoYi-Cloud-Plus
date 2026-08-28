package org.ssssssss.magicapi.iot.protocol;

import org.ssssssss.magicapi.iot.core.model.DeviceCommand;
import org.ssssssss.magicapi.iot.core.model.DeviceMessage;
import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.CommandEncoder;
import org.ssssssss.magicapi.iot.core.spi.FrameDecoder;
import org.ssssssss.magicapi.iot.core.spi.MessageDecoder;
import org.ssssssss.magicapi.iot.core.spi.ProtocolDetector;

import java.nio.ByteBuffer;
import java.util.List;

public record ProtocolPipeline(
    String protocolId,
    ProtocolDetector detector,
    FrameDecoder frameDecoder,
    MessageDecoder messageDecoder,
    CommandEncoder commandEncoder
) {
    public boolean supports(ByteBuffer input, ProtocolContext context) {
        return detector.supports(input.asReadOnlyBuffer(), context);
    }

    public List<DeviceMessage> decode(ByteBuffer input, ProtocolContext context) {
        return frameDecoder.decodeFrames(input.asReadOnlyBuffer(), context).stream()
            .map(frame -> messageDecoder.decodeMessage(frame.asReadOnlyBuffer(), context))
            .toList();
    }

    public ByteBuffer encode(DeviceCommand command, ProtocolContext context) {
        return commandEncoder.encodeCommand(command, context);
    }
}
