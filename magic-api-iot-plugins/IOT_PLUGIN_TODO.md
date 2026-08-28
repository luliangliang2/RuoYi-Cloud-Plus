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

## P0 - Provider Separation (Completed)

- [x] Keep provider contracts in `iot-core` and split concrete registry implementations.
- [x] Split session implementations into Memory and Redis providers.
- [x] Split message bus implementations into Memory, Kafka, Pulsar and RocketMQ providers.
- [x] Move registry, session and message-bus memory implementations into `providers/` modules.
- [x] Remove empty registry, session and message-bus modules from `core/`.
- [x] Require explicit provider configuration for in-memory implementations.
- [x] Reject memory providers when the active profile is `prod` or `production`.
- [x] Add Redis device registry and distributed session provider modules.
- [x] Add Kafka message bus provider publishing module.
- [x] Add Kafka dynamic subscription, manual ACK, retry, DLT and backpressure handling.
- [x] Add Pulsar and RocketMQ provider module boundaries.
- [x] Add RocketMQ publishing implementation.
- [x] Add RocketMQ dynamic consumer with native retry and DLQ behavior.
- [x] Add Pulsar producer, shared consumer, ACK, redelivery and dead-letter behavior.
- [x] Fail production startup when a required distributed provider is missing.

## P0 - Testing and Operations

- [x] Add initial `magic-api-plugin-testkit` descriptor contract assertions.
- [ ] Add integration tests using Redis, Kafka and database Testcontainers.
- [x] Add opt-in integration tests for external Redis and Kafka services.
- [x] Verify Redis Registry/Session and Kafka publish/consume/retry/DLT against live services.
- [x] Verify Pulsar publish/shared-consume/negative-ACK/redelivery against a live broker.
- [x] Verify RocketMQ publish/consume/native-retry against a live broker.
- [ ] Test duplicate IDs, missing dependencies and capability conflicts.
- [x] Validate missing and conflicting core Provider beans after singleton initialization.
- [x] Reject provider types assigned to the wrong core contract.
- [x] Run shared behavior contracts against all Memory Providers.
- [ ] Test lifecycle resource cleanup for threads, channels and subscriptions.
- [x] Add an Actuator `iotProviders` health contributor backed by the shared provider health catalog.
- [x] Add Micrometer-backed TelemetryRecorder for provider latency/status and plugin lifecycle metrics.
- [x] Instrument protocol handshake, decode, message publish and error paths with provider latency/status metrics.
- [ ] Show plugin dependencies, capabilities, version and last error in the console.

## Distributed Gateway Cluster

- [x] Add a framework-neutral gateway `NodeRegistry` contract distinct from device registry and session routing.
- [x] Add common node registration, heartbeat, discovery and graceful removal lifecycle coordination.
- [x] Add a Nacos ephemeral Naming Provider.
- [x] Add a ZooKeeper ephemeral ZNode Provider.
- [x] Add an etcd lease-backed key Provider.
- [x] Fail startup when clustering is enabled without a concrete node registry Provider.
- [x] Validate Provider endpoints, paths, durations and etcd credential pairs during startup.
- [x] Expose cluster membership and node-registry health in the test application and monitoring APIs.
- [x] Verify Nacos registration and heartbeat against `10.211.55.3:8848`.
- [x] Verify ZooKeeper ephemeral-node behavior with an embedded integration test.
- [x] Add an opt-in integration test against a live etcd cluster.
- [x] Define a separate configuration-center SPI with get, list, watch, revision and CAS operations.
- [x] Add Nacos, ZooKeeper and etcd configuration-center Providers after the SPI is stable.
- [x] Require an explicit configuration-center Provider and reject missing/conflicting Provider beans.
- [x] Expose configuration-center health and management APIs in the test gateway.
- [x] Add 5177 configuration management view with active-provider metadata and revision-safe CRUD.
- [x] Verify the etcd configuration-center Provider against a live etcd cluster.
- [x] Add node-local configuration mirror with remote watch synchronization.
- [x] Add namespaced configuration parser SPI and rule-engine JSON parser.
- [ ] Add provider-specific parsers for device registry, session, transports and protocol runtime settings.

## P0 - Extension Points (Step 6)

- [x] Split protocol processing into `ProtocolDetector`, `FrameDecoder`, `MessageDecoder` and `CommandEncoder`.
- [x] Add `TransportProvider` as the transport lifecycle and channel boundary.
- [x] Assemble protocol stages by `protocolId` and reject missing or duplicate stages.
- [x] Remove the legacy `ProtocolAdapter`, bridge and `ProtocolRegistry` compatibility layer.
- [x] Convert the raw protocol adapter to the granular protocol SPI.
- [x] Remove the empty `magic-api-plugin-protocol-sample` module.
- [x] Replace `StorageWriter` with named `StorageWriterProvider` routing and batch writes.
- [x] Replace inline rule `Consumer` actions with named `RuleActionProvider` resolution.
- [x] Add the framework-neutral `ProviderHealthIndicator` contract and Spring health catalog.
- [x] Implement real health indicators in Redis and message-bus providers.
- [x] Export provider health through Actuator and the operations console.
- [x] Add the first Netty TCP transport with line framing, lifecycle cleanup and runtime counters.
- [x] Register the Raw protocol adapter and connect transport frames to the protocol pipeline and message bus.
- [ ] Add device authentication handshake before promoting temporary TCP connection identities to registered devices.
- [x] Replace Redis Device Registry management-time `SCAN` pagination with maintained product/device indexes for large fleets.
- [x] Add Redis device index initialization, consistency verification and repair/rebuild operations.
- [x] Add an embedded-broker MQTT transport with Topic mapping, registry authentication and runtime counters.
- [x] Add an external MQTT/EMQX client transport with shared subscriptions, reconnect, registry validation and command downlink.
- [x] Add an EMQX lifecycle event adapter for authoritative device online/offline session updates.
- [x] Add UDP and WebSocket transport providers.
- [x] Add Modbus TCP as the first production protocol adapter.
- [x] Add MBAP-aware TCP stream framing, exception decoding and transaction-aware command correlation.

## P1 - Runtime Loading (Completed)

- [x] Load external plugin JARs from a configured `plugins/` directory.
- [x] Introduce one controlled class loader per external plugin.
- [x] Prevent plugins from bundling duplicate plugin API classes.
- [x] Add enable, disable, reload, upgrade and rollback operations.
- [x] Discover `IotPlugin` and `PluginService` through `ServiceLoader`.
- [x] Register and expose plugin descriptors and SPI diagnostics.
- [x] Support configuration refresh only for explicitly refreshable parsers.
- [ ] Persist plugin installation and desired-state records.

## P2 - Safe Hot Plugging

- [ ] Support hot loading for stateless protocol adapters.
- [ ] Drain commands and messages before stopping a stateful plugin.
- [ ] Migrate or close device channels before transport plugin removal.
- [x] Close external plugin class loaders during disable, reload, upgrade and shutdown.
- [ ] Detect resource leaks with thread/channel/subscription leak tests.
- [x] Add a node-by-node rolling plugin upgrade coordinator with drain, readiness, rollback and resume hooks.

## Script Orchestration

- [x] Add framework-neutral script definition, engine, registry, version and execution contracts.
- [x] Add AviatorScript, GraalVM JavaScript and Groovy engine providers.
- [x] Add draft save, validate, publish, enable/disable, rollback, delete and dry-run APIs.
- [x] Add Monaco Editor script management and dry-run console to the 5177 operations UI.
- [x] Define a standard action-plan DSL for handshake, authentication, routing and message actions.
- [x] Persist script metadata, source and version history in the etcd distributed Provider.
- [x] Add etcd CAS version protection and opt-in live cluster integration coverage for scripts.
- [ ] Add configuration-center watch integration for desired script releases and event bindings.
- [x] Add permission-aware action executor and registry contracts backed by explicit controlled SPI adapters.
- [ ] Add timeout isolation with dedicated executors/process workers and hard resource quotas.
- [x] Replace Groovy token checks with AST-level sandboxing.
- [ ] Move untrusted Groovy execution to an isolated worker with hard resource quotas.
- [ ] Add script audit logs, metrics, circuit breakers, invocation traces and cluster rolling release.

## Architecture Rules

- New modules belong under `platform/`, `core/`, `features/`, `adapters/` or `providers/` according to the classification above.
- Step 6 extension contracts are the only supported API; legacy protocol, storage and inline rule action compatibility is intentionally not retained.
- `plugin-api` must not depend on Spring, Netty, Kafka, Redis, Nacos or Magic API.
- Plugins receive controlled services through `PluginContext`; they do not access the full Spring context.
- Plugins communicate through API contracts and events, not implementation classes.
- Configuration is namespaced by plugin ID and secrets are references, not plain text.
- Runtime registration is the source of truth for monitoring; bean scanning is diagnostic only.
- Core distributed-state plugins require explicit production providers and must not silently fall back to memory.
