package org.ssssssss.magicapi.iot.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Internal node-to-node HTTP endpoint, separate from the public gateway port.
 */
public final class GatewayClusterCommunicationServer implements AutoCloseable {
	private final GatewayClusterProperties properties;
	private final GatewayNodeCoordinator coordinator;
	private final ObjectMapper mapper;
	private final AtomicBoolean draining = new AtomicBoolean();
	private volatile HttpServer server;

	public GatewayClusterCommunicationServer(GatewayClusterProperties properties, GatewayNodeCoordinator coordinator,
			ObjectMapper mapper) {
		this.properties = Objects.requireNonNull(properties, "properties");
		this.coordinator = Objects.requireNonNull(coordinator, "coordinator");
		this.mapper = Objects.requireNonNull(mapper, "mapper");
	}

	public synchronized void start() {
		if (server != null)
			return;
		try {
			HttpServer current = HttpServer.create(
					new InetSocketAddress(properties.getCommunicationBindAddress(), properties.getCommunicationPort()),
					32);
			current.createContext("/internal/cluster/ping", this::ping);
			current.createContext("/internal/cluster/status", this::status);
			current.createContext("/internal/cluster/drain", this::drain);
			current.createContext("/internal/cluster/resume", this::resume);
			current.setExecutor(Executors.newCachedThreadPool(r -> {
				Thread t = new Thread(r, "iot-cluster-http");
				t.setDaemon(true);
				return t;
			}));
			current.start();
			server = current;
		} catch (IOException e) {
			throw new IllegalStateException(
					"Failed to bind cluster communication port " + properties.getCommunicationPort(), e);
		}
	}

	public boolean isDraining() {
		return draining.get();
	}

	private void ping(HttpExchange e) throws IOException {
		respond(e, 200, Map.of("status", "UP", "nodeId", properties.getNodeId(), "draining", draining.get()));
	}

	private void status(HttpExchange e) throws IOException {
		respond(e, 200, Map.of("nodeId", properties.getNodeId(), "draining", draining.get(), "cluster",
				coordinator.snapshot(), "activeNodes", coordinator.activeNodes()));
	}

	private void drain(HttpExchange e) throws IOException {
		if (!authorized(e)) {
			respond(e, 401, Map.of("error", "unauthorized"));
			return;
		}
		draining.set(true);
		respond(e, 200, Map.of("status", "DRAINING"));
	}

	private void resume(HttpExchange e) throws IOException {
		if (!authorized(e)) {
			respond(e, 401, Map.of("error", "unauthorized"));
			return;
		}
		draining.set(false);
		respond(e, 200, Map.of("status", "READY"));
	}

	private boolean authorized(HttpExchange e) {
		String expected = properties.getCommunicationToken();
		if (expected == null || expected.isBlank())
			return true;
		String actual = e.getRequestHeaders().getFirst("X-Iot-Cluster-Token");
		return actual != null && MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
				actual.getBytes(StandardCharsets.UTF_8));
	}

	private void respond(HttpExchange e, int status, Object body) throws IOException {
		byte[] bytes = mapper.writeValueAsBytes(body);
		e.getResponseHeaders().set("Content-Type", "application/json");
		e.sendResponseHeaders(status, bytes.length);
		try (var output = e.getResponseBody()) {
			output.write(bytes);
		}
	}

	@Override
	public synchronized void close() {
		HttpServer current = server;
		server = null;
		if (current != null)
			current.stop(1);
	}
}
