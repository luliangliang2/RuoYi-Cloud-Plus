package org.ssssssss.magicapi.iot.transport.tcp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("iot.transports.modbus-tcp")
public class ModbusTcpTransportProperties extends TcpTransportProperties {
    public ModbusTcpTransportProperties() {
        setTransportId("modbus-tcp");
        setProtocolId("modbus-tcp");
        setFrameMode(TcpFrameMode.MODBUS_TCP);
        setPort(1502);
        setMaxFrameLength(260);
    }
}
