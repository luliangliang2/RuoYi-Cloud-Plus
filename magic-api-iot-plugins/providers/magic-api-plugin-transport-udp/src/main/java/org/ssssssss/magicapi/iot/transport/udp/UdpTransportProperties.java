package org.ssssssss.magicapi.iot.transport.udp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("iot.transports.udp")
public class UdpTransportProperties {
    private boolean enabled; private String host = "0.0.0.0"; private int port = 19001;
    private int maxDatagramSize = 65507; private String protocolId = "raw";
    public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;}
    public String getHost(){return host;} public void setHost(String v){host=v;}
    public int getPort(){return port;} public void setPort(int v){port=v;}
    public int getMaxDatagramSize(){return maxDatagramSize;} public void setMaxDatagramSize(int v){maxDatagramSize=v;}
    public String getProtocolId(){return protocolId;} public void setProtocolId(String v){protocolId=v;}
}
