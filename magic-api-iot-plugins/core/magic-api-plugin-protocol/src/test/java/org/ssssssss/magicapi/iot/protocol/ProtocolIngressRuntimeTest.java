package org.ssssssss.magicapi.iot.protocol;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtocolIngressRuntimeTest {
    @Test
    void decodesTransportPayloadAndPublishesUnifiedMessage() {
        RawExtension extension = new RawExtension();
        ProtocolPipelineRegistry registry = new ProtocolPipelineRegistry(
            List.of(extension), List.of(extension), List.of(extension), List.of(extension));
        CapturingMessageBus messageBus = new CapturingMessageBus();
        ProtocolIngressRuntime runtime = new ProtocolIngressRuntime(registry, messageBus, List.of());
        ProtocolContext context = new ProtocolContext("tcp", "127.0.0.1:10000",
            new DeviceIdentity("raw", "connection"), Map.of());

        runtime.start();
        runtime.received("connection", ByteBuffer.wrap(new byte[]{1, 2, 3}), context);

        assertEquals(1, messageBus.messages.size());
        assertEquals(1, runtime.snapshot().receivedFrames());
        assertEquals(1, runtime.snapshot().publishedMessages());
        assertEquals("raw", messageBus.messages.get(0).protocol());
    }

    private static final class RawExtension implements ProtocolDetector, FrameDecoder, MessageDecoder, CommandEncoder {
        public String protocolId() { return "raw"; }
        public boolean supports(ByteBuffer input, ProtocolContext context) { return input.hasRemaining(); }
        public List<ByteBuffer> decodeFrames(ByteBuffer input, ProtocolContext context) { return List.of(input); }
        public DeviceMessage decodeMessage(ByteBuffer frame, ProtocolContext context) {
            byte[] payload = new byte[frame.remaining()];
            frame.get(payload);
            return new DeviceMessage(null, context.device(), DeviceMessageType.RAW, "raw", null, null, payload, Map.of());
        }
        public ByteBuffer encodeCommand(DeviceCommand command, ProtocolContext context) { return ByteBuffer.allocate(0); }
    }

    private static final class CapturingMessageBus implements DeviceMessageBus {
        private final List<DeviceMessage> messages = new ArrayList<>();
        public void publish(DeviceMessage message) { messages.add(message); }
        public Subscription subscribe(String subscriberId, Consumer<DeviceMessage> consumer) { return () -> { }; }
    }
}
