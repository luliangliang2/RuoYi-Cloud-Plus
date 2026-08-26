package org.ssssssss.magicapi.iot.protocol.adapter;

import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.CommandEncoder;
import org.ssssssss.magicapi.iot.core.spi.FrameDecoder;
import org.ssssssss.magicapi.iot.core.spi.MessageDecoder;
import org.ssssssss.magicapi.iot.core.spi.ProtocolDetector;

import java.nio.ByteBuffer;
import java.util.List;

public class RawProtocolAdapter implements ProtocolDetector, FrameDecoder, MessageDecoder, CommandEncoder {
    private final String id;
    public RawProtocolAdapter(String id) { this.id = id; }
    public String protocolId() { return id; }
    public boolean supports(ByteBuffer input, ProtocolContext context) { return input != null && input.hasRemaining(); }
    public List<ByteBuffer> decodeFrames(ByteBuffer input, ProtocolContext context) { return List.of(input.asReadOnlyBuffer()); }
    public DeviceMessage decodeMessage(ByteBuffer frame, ProtocolContext context) {
        byte[] bytes = new byte[frame.remaining()]; frame.get(bytes);
        return new DeviceMessage(null, context.device(), DeviceMessageType.RAW, id, null, null, bytes, context.attributes());
    }
    public ByteBuffer encodeCommand(DeviceCommand command, ProtocolContext context) {
        if (command.payload() instanceof byte[] bytes) return ByteBuffer.wrap(bytes);
        throw new IllegalArgumentException("Raw protocol requires byte[] payload");
    }
}
