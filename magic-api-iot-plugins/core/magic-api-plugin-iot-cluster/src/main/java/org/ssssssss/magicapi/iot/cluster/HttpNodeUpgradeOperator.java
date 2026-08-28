package org.ssssssss.magicapi.iot.cluster;

import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Remote operator for gateways exposing authenticated plugin lifecycle
 * endpoints.
 */
public final class HttpNodeUpgradeOperator implements RollingPluginUpgradeCoordinator.NodeUpgradeOperator {
	private final HttpClient client;
	private final Duration timeout;

	public HttpNodeUpgradeOperator(Duration timeout) {
		this.timeout = timeout == null ? Duration.ofSeconds(10) : timeout;
		this.client = HttpClient.newBuilder().connectTimeout(this.timeout).build();
	}

	@Override
	public void drain(NodeRegistry.GatewayNode node) throws Exception {
		call(node, "POST", "/api/iot/gateway/plugins/drain", "{}");
	}

	@Override
	public void upgrade(NodeRegistry.GatewayNode node, String pluginId, Path jar) throws Exception {
		call(node, "POST", "/api/iot/gateway/plugins/upgrade",
				"{\"pluginId\":\"" + escape(pluginId) + "\",\"jar\":\"" + escape(jar.toString()) + "\"}");
	}

	@Override
	public void awaitReady(NodeRegistry.GatewayNode node) throws Exception {
		call(node, "GET", "/actuator/health/iotProviders", "");
	}

	@Override
	public void resume(NodeRegistry.GatewayNode node) throws Exception {
		call(node, "POST", "/api/iot/gateway/plugins/resume", "{}");
	}

	@Override
	public void rollback(NodeRegistry.GatewayNode node, String pluginId) throws Exception {
		call(node, "POST", "/api/iot/gateway/plugins/" + escape(pluginId) + "/rollback", "{}");
	}

	private void call(NodeRegistry.GatewayNode node, String method, String path, String body) throws Exception {
		URI uri = URI.create((node.address().startsWith("http") ? node.address() : "http://" + node.address()) + path);
		HttpRequest.Builder request = HttpRequest.newBuilder(uri).timeout(timeout).header("Content-Type",
				"application/json");
		if ("GET".equals(method))
			request.GET();
		else
			request.method(method, HttpRequest.BodyPublishers.ofString(body));
		HttpResponse<String> response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() < 200 || response.statusCode() >= 300)
			throw new IllegalStateException("Remote node " + node.nodeId() + " returned HTTP " + response.statusCode());
	}

	private static String escape(String value) {
		return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
