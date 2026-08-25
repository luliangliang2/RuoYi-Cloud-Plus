# IoT Plugin Architecture TODO

## Target

Build the IoT gateway as a plugin platform with a stable API, explicit plugin metadata,
controlled lifecycle, capability discovery, dependency validation and observable runtime state.
Business concepts such as tenant or household must stay outside the plugin platform.

## P0 - Plugin Foundation

- [x] Add an independent `magic-api-plugin-api` module without Spring dependencies.
- [x] Define plugin descriptor, lifecycle, context, health and runtime state contracts.
- [x] Add `magic-api-plugin-runtime` for descriptor discovery and plugin registration.
- [x] Add a capability registry instead of inferring capabilities from Spring bean names.
- [x] Add `magic-api-plugin-spring-boot-starter` as the Spring integration boundary.
- [x] Keep `magic-api-plugin-iot-core` as a compatibility layer during migration.
- [x] Add `META-INF/iot-plugin.json` descriptors to current P0 plugins.
- [x] Expose runtime plugin data from the test application's monitoring endpoint.
- [ ] Move generic device models and SPIs from `iot-core` to `plugin-api` in a compatibility-safe release.
- [ ] Add descriptor JSON Schema and Maven build-time validation.
- [ ] Add semantic API version and dependency range validation.
- [ ] Detect dependency cycles and conflicting singleton capabilities.
- [ ] Add required, optional and degraded plugin failure policies.

## P0 - Provider Separation

- [ ] Split API and implementations: `registry-api`, `registry-memory`, `registry-jdbc`.
- [ ] Split session implementations: `session-memory`, `session-redis`.
- [ ] Split message bus implementations: Kafka, Pulsar and RocketMQ.
- [x] Move registry, session and message-bus memory implementations into `providers/` modules.
- [x] Require explicit provider configuration for in-memory implementations.
- [x] Reject memory providers when the active profile is `prod` or `production`.
- [ ] Add distributed Redis/JDBC/Kafka provider modules.
- [ ] Fail production startup when a required distributed provider is missing.

## P0 - Testing and Operations

- [ ] Add `magic-api-plugin-testkit` with lifecycle and descriptor contract tests.
- [ ] Add integration tests using Redis, Kafka and database Testcontainers.
- [ ] Test duplicate IDs, missing dependencies and capability conflicts.
- [ ] Test lifecycle resource cleanup for threads, channels and subscriptions.
- [ ] Add Actuator health contributors and Micrometer lifecycle metrics.
- [ ] Show plugin dependencies, capabilities, version and last error in the console.

## P1 - Runtime Loading

- [ ] Load external plugin JARs from a configured `plugins/` directory.
- [ ] Introduce one controlled class loader per external plugin.
- [ ] Prevent plugins from bundling duplicate plugin API classes.
- [ ] Add install, enable, disable, upgrade and rollback operations.
- [ ] Support configuration refresh only for explicitly refreshable fields.
- [ ] Persist plugin installation and desired-state records.

## P2 - Safe Hot Plugging

- [ ] Support hot loading for stateless protocol adapters.
- [ ] Drain commands and messages before stopping a stateful plugin.
- [ ] Migrate or close device channels before transport plugin removal.
- [ ] Verify class-loader release and detect resource leaks.
- [ ] Support rolling plugin upgrades across gateway nodes.

## Architecture Rules

- New modules belong under `platform/`, `core/`, `features/` or `adapters/` according to the classification above.
- `plugin-api` must not depend on Spring, Netty, Kafka, Redis, Nacos or Magic API.
- Plugins receive controlled services through `PluginContext`; they do not access the full Spring context.
- Plugins communicate through API contracts and events, not implementation classes.
- Configuration is namespaced by plugin ID and secrets are references, not plain text.
- Runtime registration is the source of truth for monitoring; bean scanning is diagnostic only.
- Core distributed-state plugins require explicit production providers and must not silently fall back to memory.
