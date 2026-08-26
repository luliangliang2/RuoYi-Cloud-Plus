package org.ssssssss.magicapi.iot.protocol.modbus;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModbusTcpProtocolAdapterTest {
    private final ModbusTcpProtocolAdapter adapter = new ModbusTcpProtocolAdapter();
    private final DeviceIdentity device = new DeviceIdentity("plc", "line-1");

    @Test
    void detectsAndDecodesReadHoldingRegistersRequest() {
        byte[] bytes = {0, 7, 0, 0, 0, 6, 1, 3, 0, 10, 0, 2};
        ProtocolContext context = context("server");
        assertTrue(adapter.supports(ByteBuffer.wrap(bytes), context));
        DeviceMessage message = adapter.decodeMessage(ByteBuffer.wrap(bytes), context);
        ModbusTcpPayload payload = (ModbusTcpPayload) message.payload();
        assertEquals("request", payload.direction());
        assertEquals(7, payload.transactionId());
        assertEquals(3, payload.functionCode());
        assertTrue(payload.decoded().contains("address=10"));
        assertEquals(DeviceMessageType.RAW, message.type());
    }

    @Test
    void encodesAllSupportedCommandFamilies() {
        assertFunction("readCoils", Map.of("address", 0, "quantity", 8), 1);
        assertFunction("readDiscreteInputs", Map.of("address", 0, "quantity", 8), 2);
        assertFunction("readHoldingRegisters", Map.of("address", 0, "quantity", 2), 3);
        assertFunction("readInputRegisters", Map.of("address", 0, "quantity", 2), 4);
        assertFunction("writeSingleCoil", Map.of("address", 1, "value", true), 5);
        assertFunction("writeSingleRegister", Map.of("address", 1, "value", 123), 6);
        assertFunction("writeMultipleCoils", Map.of("address", 1, "quantity", 8, "values", new byte[]{1}), 15);
        assertFunction("writeMultipleRegisters", Map.of("address", 1, "quantity", 2, "values", new byte[]{0, 1, 0, 2}), 16);
    }

    @Test
    void correlatesResponseAndReportsModbusException() {
        DeviceCommand command = command("readHoldingRegisters", Map.of(
            "transactionId", 42, "unitId", 1, "address", 0, "quantity", 1));
        adapter.encodeCommand(command, context("client"));

        byte[] response = {0, 42, 0, 0, 0, 5, 1, 3, 2, 0, 11};
        DeviceMessage message = adapter.decodeMessage(ByteBuffer.wrap(response), context("client"));
        assertEquals(command.commandId(), message.metadata().get("commandId"));
        assertEquals(DeviceMessageType.COMMAND_REPLY, message.type());

        byte[] exception = {0, 43, 0, 0, 0, 3, 1, (byte) 0x83, 2};
        ModbusTcpPayload payload = (ModbusTcpPayload) adapter.decodeMessage(
            ByteBuffer.wrap(exception), context("client")).payload();
        assertTrue(payload.exception());
        assertEquals(2, payload.exceptionCode());
    }

    private void assertFunction(String action, Map<String, Object> values, int function) {
        ByteBuffer frame = adapter.encodeCommand(command(action, values), context("client"));
        assertEquals(0, Short.toUnsignedInt(frame.getShort(2)));
        assertEquals(function, Byte.toUnsignedInt(frame.get(7)));
        assertEquals(frame.remaining(), 6 + Short.toUnsignedInt(frame.getShort(4)));
    }

    private DeviceCommand command(String action, Map<String, Object> payload) {
        return new DeviceCommand(null, device, action, payload, 1, Duration.ofSeconds(2), null, Map.of());
    }

    private ProtocolContext context(String role) {
        return new ProtocolContext("modbus-tcp", "127.0.0.1:502", device,
            Map.of("protocolRole", role, "connectionId", "test"));
    }
}
