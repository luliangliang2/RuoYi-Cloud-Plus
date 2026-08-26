package org.ssssssss.magicapi.iot.transport.tcp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("iot.transports.tcp")
public class TcpTransportProperties {
    private boolean enabled;
    private String host = "0.0.0.0";
    private int port = 19000;
    private int maxFrameLength = 65536;
    private String transportId = "tcp";
    private String protocolId = "raw";
    private String protocolRole = "server";
    private TcpFrameMode frameMode = TcpFrameMode.LINE;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public int getMaxFrameLength() { return maxFrameLength; }
    public void setMaxFrameLength(int maxFrameLength) { this.maxFrameLength = maxFrameLength; }
    public String getTransportId() { return transportId; }
    public void setTransportId(String transportId) { this.transportId = transportId; }
    public String getProtocolId() { return protocolId; }
    public void setProtocolId(String protocolId) { this.protocolId = protocolId; }
    public String getProtocolRole() { return protocolRole; }
    public void setProtocolRole(String protocolRole) { this.protocolRole = protocolRole; }
    public TcpFrameMode getFrameMode() { return frameMode; }
    public void setFrameMode(TcpFrameMode frameMode) { this.frameMode = frameMode; }
}
