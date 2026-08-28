package org.ssssssss.magicapi.iot.protocol.modbus;

public record ModbusTcpPayload(
    String direction,
    int transactionId,
    int unitId,
    int functionCode,
    String function,
    boolean exception,
    Integer exceptionCode,
    byte[] data,
    String decoded
) { }
