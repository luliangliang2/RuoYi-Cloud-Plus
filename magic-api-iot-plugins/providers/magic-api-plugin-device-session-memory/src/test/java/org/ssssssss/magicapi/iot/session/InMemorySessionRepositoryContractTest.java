package org.ssssssss.magicapi.iot.session;

import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.testkit.SessionRepositoryContract;

class InMemorySessionRepositoryContractTest extends SessionRepositoryContract {
	private final SessionRepository value = new InMemorySessionRepository();

	protected SessionRepository repository() {
		return value;
	}
}
