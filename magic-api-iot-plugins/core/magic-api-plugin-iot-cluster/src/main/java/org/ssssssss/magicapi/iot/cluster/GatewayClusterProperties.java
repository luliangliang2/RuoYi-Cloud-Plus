package org.ssssssss.magicapi.iot.cluster;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties("iot.cluster")
public class GatewayClusterProperties {
	private boolean enabled;
	private String nodeId = "gateway-node-1";
	private String address = "127.0.0.1:9218";
	private int capacity = 10000;
	private Duration heartbeatInterval = Duration.ofSeconds(5);
	private String communicationBindAddress = "0.0.0.0";
	private int communicationPort = 19218;
	private String communicationAdvertiseAddress;
	private String communicationToken;
	private Map<String, String> metadata = new LinkedHashMap<>();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Duration getHeartbeatInterval() {
		return heartbeatInterval;
	}

	public void setHeartbeatInterval(Duration heartbeatInterval) {
		this.heartbeatInterval = heartbeatInterval;
	}
	public String getCommunicationBindAddress() { return communicationBindAddress; }
	public void setCommunicationBindAddress(String value) { this.communicationBindAddress = value; }
	public int getCommunicationPort() { return communicationPort; }
	public void setCommunicationPort(int value) { this.communicationPort = value; }
	public String getCommunicationAdvertiseAddress() { return communicationAdvertiseAddress; }
	public void setCommunicationAdvertiseAddress(String value) { this.communicationAdvertiseAddress = value; }
	public String getCommunicationToken() { return communicationToken; }
	public void setCommunicationToken(String value) { this.communicationToken = value; }

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
}
