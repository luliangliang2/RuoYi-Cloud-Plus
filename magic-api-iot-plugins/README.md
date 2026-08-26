# Magic API IoT Plugins

Independent IoT plugin suite for the adjacent `magic-api` project. This reactor deliberately does not modify or require changes inside `magic-api`.

## Modules

- `platform/`: plugin API, runtime and Spring Boot starter
- `core/`: gateway model/SPI, protocol, command, observability and security
- `features/`: device shadow, product model, rule engine, OTA, time-series, storage, northbound and ops console
- `adapters/`: protocol adapter modules built from detector, frame decoder, message decoder and command encoder extensions
- `providers/`: replaceable Memory, Redis, Kafka, Pulsar and RocketMQ infrastructure providers

Device registry, device session and message bus are provider extension points. Their contracts
remain in `magic-api-plugin-iot-core`; concrete implementations do not live in `core/`.

All new modules must be placed under one of these five directories. The directory is the
delivery classification; the Maven artifactId remains stable and is used for dependencies.

Provider selection is explicit. Core modules do not create in-memory implementations by default:

```yaml
iot:
  providers:
    device-registry:
      type: redis
    device-session:
      type: redis
    message-bus:
      type: kafka
```

The memory providers are separate modules and must be enabled explicitly with `type: memory`.
They are rejected when the active Spring profile is `prod` or `production`.

Provider health probes are read-only and shared by the gateway API and Actuator. Redis uses
`PING`; Kafka and Pulsar query configured Topic metadata; RocketMQ checks the configured Topic
route and reports `DEGRADED` when the broker is reachable but the business Topic has no route.

```yaml
iot:
  health:
    cache-ttl: 10s
    timeout: 3s
```

The Spring integration exposes the aggregate at `/actuator/health` as `iotProviders`. The test
gateway also exposes `/api/iot/gateway/providers`, which is consumed by the console on port 5177.
Redis connection infrastructure is created by `magic-api-plugin-redis-support` only when the
registry or session Provider type is `redis`.

## Raw TCP transport

The first executable protocol path is Raw over Netty TCP. It uses newline-delimited frames and
publishes decoded `DeviceMessage` instances to the selected message bus.

```yaml
iot:
  transports:
    tcp:
      enabled: true
      host: 0.0.0.0
      port: 19000
      max-frame-length: 65536
```

```bash
printf 'temperature=23.5\n' | nc -w 1 127.0.0.1 19000
curl http://127.0.0.1:9218/api/iot/gateway/runtime
```

Raw TCP currently assigns `tcp-raw/<channelId>` as a temporary connection identity. It is an
integration path, not an authenticated production device protocol. A protocol-specific
authentication handshake must replace the temporary identity before enabling untrusted clients.

External Redis and Kafka integration tests are opt-in:

```bash
mvn test -Diot.integration.enabled=true \
  -Diot.redis.host=10.211.55.4 -Diot.redis.port=6379 \
  -Diot.kafka.bootstrap=10.211.55.4:9092
```

## Build

```bash
mvn test
```

## Plugin architecture

```text
magic-api-plugin-api
        ^
        |
magic-api-plugin-runtime
        ^
        |
magic-api-plugin-spring-boot-starter

magic-api-plugin-iot-core -> magic-api-plugin-api
feature plugins           -> magic-api-plugin-iot-core
```

`magic-api-plugin-api` contains the stable, Spring-free plugin contract. The runtime discovers
`META-INF/iot-plugin.json`, validates plugin identity and required dependencies, and registers
capabilities. The Spring Boot starter is only an integration boundary for auto-configuration.

Existing feature plugins still depend on `magic-api-plugin-iot-core` during the compatibility
phase. Generic device models and SPIs will move into the API module in a later compatible release.
See [IOT_PLUGIN_TODO.md](IOT_PLUGIN_TODO.md) for migration status and architecture rules.
See [IOT_PLUGIN_PROGRESS.md](IOT_PLUGIN_PROGRESS.md) for the long-term implementation baseline and update history.
