package org.ssssssss.magicapi.iot.protocol;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolPipelineRegistryTest {
    private final DeviceIdentity device = new DeviceIdentity("product", "device");
    private final ProtocolContext context = new ProtocolContext("tcp", "127.0.0.1", device, Map.of());

    @Test void assemblesGranularExtensionsAndProcessesMessages() {
        TestProtocol extension = new TestProtocol("test", 10);
        ProtocolPipelineRegistry registry = new ProtocolPipelineRegistry(
            List.of(extension), List.of(extension), List.of(extension), List.of(extension));
        ProtocolPipeline pipeline = registry.detect(ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8)), context).orElseThrow();
        assertEquals("test", pipeline.protocolId());
        assertEquals("hello", pipeline.decode(ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8)), context).get(0).payload());
        DeviceCommand command = new DeviceCommand(null, device, "write", "world", 1, Duration.ofSeconds(1), null, Map.of());
        assertEquals("world", StandardCharsets.UTF_8.decode(pipeline.encode(command, context)).toString());
    }

    @Test void selectsHighestPriorityDetector() {
        TestProtocol low = new TestProtocol("low", 1);
        TestProtocol high = new TestProtocol("high", 100);
        ProtocolPipelineRegistry registry = new ProtocolPipelineRegistry(
            List.of(low, high), List.of(low, high), List.of(low, high), List.of(low, high));
        assertEquals("high", registry.detect(ByteBuffer.wrap(new byte[]{1}), context).orElseThrow().protocolId());
    }

    @Test void rejectsIncompleteGranularProtocol() {
        TestProtocol extension = new TestProtocol("broken", 0);
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
            () -> new ProtocolPipelineRegistry(List.of(extension), List.of(), List.of(), List.of()));
        assertTrue(error.getMessage().contains("frame decoder"));
    }

    private static class TestProtocol implements ProtocolDetector, FrameDecoder, MessageDecoder, CommandEncoder {
        private final String id;
        private final int priority;
        private TestProtocol(String id, int priority) { this.id = id; this.priority = priority; }
        public String protocolId() { return id; }
        public int priority() { return priority; }
        public boolean supports(ByteBuffer input, ProtocolContext context) { return input.hasRemaining(); }
        public List<ByteBuffer> decodeFrames(ByteBuffer input, ProtocolContext context) { return List.of(input); }
        public DeviceMessage decodeMessage(ByteBuffer frame, ProtocolContext context) {
            return new DeviceMessage(null, context.device(), DeviceMessageType.RAW, id, null, null,
                StandardCharsets.UTF_8.decode(frame).toString(), Map.of());
        }
        public ByteBuffer encodeCommand(DeviceCommand command, ProtocolContext context) {
            return ByteBuffer.wrap(command.payload().toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
