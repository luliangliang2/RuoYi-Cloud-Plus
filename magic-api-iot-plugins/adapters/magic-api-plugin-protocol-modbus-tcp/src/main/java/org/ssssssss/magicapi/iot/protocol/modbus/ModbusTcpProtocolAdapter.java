package org.ssssssss.magicapi.iot.protocol.modbus;

import com.digitalpetri.modbus.FunctionCode;
import com.digitalpetri.modbus.ModbusPduSerializer;
import com.digitalpetri.modbus.pdu.*;
import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class ModbusTcpProtocolAdapter implements ProtocolDetector, FrameDecoder, MessageDecoder, CommandEncoder {
    public static final String PROTOCOL_ID = "modbus-tcp";
    private static final int MIN_FRAME_LENGTH = 8;
    private static final int MAX_LENGTH_FIELD = 254;
    private final AtomicInteger transactions = new AtomicInteger();
    private final Map<Integer, String> commandCorrelations = new ConcurrentHashMap<>();

    @Override public String protocolId() { return PROTOCOL_ID; }
    @Override public int priority() { return 100; }

    @Override
    public boolean supports(ByteBuffer input, ProtocolContext context) {
        if (input == null || input.remaining() < MIN_FRAME_LENGTH) return false;
        ByteBuffer frame = input.asReadOnlyBuffer();
        int position = frame.position();
        int protocolId = Short.toUnsignedInt(frame.getShort(position + 2));
        int length = Short.toUnsignedInt(frame.getShort(position + 4));
        return protocolId == 0 && length >= 2 && length <= MAX_LENGTH_FIELD
            && frame.remaining() == 6 + length;
    }

    @Override
    public List<ByteBuffer> decodeFrames(ByteBuffer input, ProtocolContext context) {
        if (!supports(input, context)) throw new IllegalArgumentException("Invalid Modbus TCP MBAP frame");
        return List.of(input.asReadOnlyBuffer());
    }

    @Override
    public DeviceMessage decodeMessage(ByteBuffer frame, ProtocolContext context) {
        ByteBuffer input = frame.asReadOnlyBuffer();
        int transactionId = Short.toUnsignedInt(input.getShort());
        int protocolId = Short.toUnsignedInt(input.getShort());
        int length = Short.toUnsignedInt(input.getShort());
        int unitId = Byte.toUnsignedInt(input.get());
        int functionCode = Byte.toUnsignedInt(input.get());
        if (protocolId != 0 || length != frame.remaining() - 6) {
            throw new IllegalArgumentException("Invalid Modbus TCP header");
        }

        boolean exception = (functionCode & 0x80) != 0;
        int baseFunctionCode = functionCode & 0x7f;
        byte[] data = new byte[input.remaining()];
        input.asReadOnlyBuffer().get(data);
        String direction = "client".equalsIgnoreCase(context.attributes().getOrDefault("protocolRole", "server"))
            ? "response" : "request";
        Integer exceptionCode = exception && data.length > 0 ? Byte.toUnsignedInt(data[0]) : null;
        String decoded = decodePdu(direction, baseFunctionCode, data, exception);
        String function = FunctionCode.from(baseFunctionCode).map(Enum::name).orElse("UNKNOWN");
        String commandId = "response".equals(direction) ? commandCorrelations.remove(transactionId) : null;

        Map<String, String> metadata = new HashMap<>(context.attributes());
        metadata.put("transactionId", Integer.toString(transactionId));
        metadata.put("unitId", Integer.toString(unitId));
        metadata.put("functionCode", Integer.toString(baseFunctionCode));
        metadata.put("direction", direction);
        metadata.put("exception", Boolean.toString(exception));
        if (commandId != null) metadata.put("commandId", commandId);
        if (exceptionCode != null) metadata.put("exceptionCode", exceptionCode.toString());

        ModbusTcpPayload payload = new ModbusTcpPayload(direction, transactionId, unitId, baseFunctionCode,
            function, exception, exceptionCode, data, decoded);
        return new DeviceMessage(null, context.device(), "response".equals(direction)
            ? DeviceMessageType.COMMAND_REPLY : DeviceMessageType.RAW, PROTOCOL_ID, Instant.now(),
            (long) transactionId, payload, metadata);
    }

    @Override
    public ByteBuffer encodeCommand(DeviceCommand command, ProtocolContext context) {
        Map<String, Object> values = values(command.payload());
        int transactionId = integer(values, "transactionId", nextTransactionId());
        int unitId = integer(values, "unitId", integer(command.metadata(), "unitId", 1));
        ModbusRequestPdu request = request(command.action(), values);
        ByteBuffer pdu = ByteBuffer.allocate(253);
        try {
            ModbusPduSerializer.DefaultRequestSerializer.INSTANCE.encode(request, pdu);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to encode Modbus command " + command.action(), exception);
        }
        pdu.flip();
        ByteBuffer frame = ByteBuffer.allocate(7 + pdu.remaining());
        frame.putShort((short) transactionId).putShort((short) 0)
            .putShort((short) (1 + pdu.remaining())).put((byte) unitId).put(pdu).flip();
        commandCorrelations.put(transactionId, command.commandId());
        return frame;
    }

    private static String decodePdu(String direction, int functionCode, byte[] data, boolean exception) {
        if (exception) return "exception:" + (data.length == 0 ? "missing-code" : Byte.toUnsignedInt(data[0]));
        try {
            ByteBuffer completePdu = ByteBuffer.allocate(1 + data.length).put((byte) functionCode).put(data).flip();
            ModbusPdu pdu = "response".equals(direction)
                ? ModbusPduSerializer.DefaultResponseSerializer.INSTANCE.decode(functionCode, completePdu)
                : ModbusPduSerializer.DefaultRequestSerializer.INSTANCE.decode(functionCode, completePdu);
            return pdu.toString();
        } catch (Exception decodeFailure) {
            throw new IllegalArgumentException("Invalid Modbus PDU for function " + functionCode, decodeFailure);
        }
    }

    private static ModbusRequestPdu request(String action, Map<String, Object> values) {
        int address = integer(values, "address", 0);
        int quantity = integer(values, "quantity", 1);
        return switch (normalize(action)) {
            case "readcoils" -> new ReadCoilsRequest(address, quantity);
            case "readdiscreteinputs" -> new ReadDiscreteInputsRequest(address, quantity);
            case "readholdingregisters" -> new ReadHoldingRegistersRequest(address, quantity);
            case "readinputregisters" -> new ReadInputRegistersRequest(address, quantity);
            case "writesinglecoil" -> new WriteSingleCoilRequest(address, bool(values, "value"));
            case "writesingleregister" -> new WriteSingleRegisterRequest(address, integer(values, "value", 0));
            case "writemultiplecoils" -> new WriteMultipleCoilsRequest(address, quantity, bytes(values.get("values")));
            case "writemultipleregisters" -> new WriteMultipleRegistersRequest(address, quantity, bytes(values.get("values")));
            default -> throw new IllegalArgumentException("Unsupported Modbus command action: " + action);
        };
    }

    private int nextTransactionId() { return transactions.updateAndGet(current -> current >= 0xffff ? 1 : current + 1); }
    private static String normalize(String value) { return value.replace("-", "").replace("_", "").toLowerCase(Locale.ROOT); }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> values(Object payload) {
        if (payload == null) return Map.of();
        if (payload instanceof Map<?, ?> map) return (Map<String, Object>) map;
        throw new IllegalArgumentException("Modbus command payload must be a Map");
    }

    private static int integer(Map<?, ?> values, String key, int defaultValue) {
        Object value = values.get(key);
        return value instanceof Number number ? number.intValue()
            : value instanceof String text && !text.isBlank() ? Integer.parseInt(text) : defaultValue;
    }

    private static boolean bool(Map<String, Object> values, String key) {
        Object value = values.get(key);
        return value instanceof Boolean flag ? flag
            : value instanceof Number number ? number.intValue() != 0
            : Boolean.parseBoolean(String.valueOf(value));
    }

    private static byte[] bytes(Object value) {
        if (value instanceof byte[] bytes) return bytes;
        if (value instanceof Collection<?> collection) {
            byte[] bytes = new byte[collection.size()];
            int index = 0;
            for (Object item : collection) bytes[index++] = ((Number) item).byteValue();
            return bytes;
        }
        throw new IllegalArgumentException("Modbus values must be byte[] or a numeric collection");
    }
}
