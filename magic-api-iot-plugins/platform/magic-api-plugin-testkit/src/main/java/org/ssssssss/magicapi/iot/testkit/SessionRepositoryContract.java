package org.ssssssss.magicapi.iot.testkit;

import org.junit.jupiter.api.Test;
import org.ssssssss.magicapi.iot.core.model.*;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import java.time.Instant;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public abstract class SessionRepositoryContract {
	protected abstract SessionRepository repository();

	@Test
	void registersIndexesTouchesAndRemovesSession() {
		DeviceIdentity device = new DeviceIdentity("product", "device-1");
		DeviceSession session = new DeviceSession("session-1", device, "node-1", "tcp", "127.0.0.1",
				Instant.parse("2026-01-01T00:00:00Z"), Instant.parse("2026-01-01T00:00:00Z"), Map.of());
		repository().register(session);
		assertEquals("session-1", repository().find(device).orElseThrow().sessionId());
		assertEquals(1, repository().findByGatewayNode("node-1").size());
		repository().touch("session-1");
		assertTrue(repository().find(device).orElseThrow().lastSeenAt().isAfter(session.lastSeenAt()));
		repository().remove("session-1");
		assertTrue(repository().find(device).isEmpty());
	}
}
