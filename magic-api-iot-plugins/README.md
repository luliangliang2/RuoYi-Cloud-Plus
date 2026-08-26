# Magic API IoT Plugins

Independent IoT plugin suite for the adjacent `magic-api` project. This reactor deliberately does not modify or require changes inside `magic-api`.

## Modules

- `platform/`: plugin API, runtime and Spring Boot starter
- `core/`: gateway model/SPI, protocol, command, observability and security
- `features/`: device shadow, product model, rule engine, OTA, time-series, storage, northbound and ops console
- `adapters/`: protocol adapter modules built from detector, frame decoder, message decoder and command encoder extensions
- `providers/`: replaceable infrastructure and TCP/MQTT transport providers

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
    node-registry:
      type: nacos
    configuration-center:
      type: nacos
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

## Distributed gateway node registry

Gateway node discovery is independent from device registration and device session routing.
`magic-api-plugin-iot-cluster` owns the common startup registration, heartbeat, discovery and
graceful removal lifecycle. Select exactly one node registry Provider:

```yaml
iot:
  cluster:
    enabled: true
    node-id: gateway-node-01
    address: 10.0.0.11:9218
    heartbeat-interval: 5s
    capacity: 10000
  providers:
    node-registry:
      type: nacos # nacos | zookeeper | etcd

  node-registry:
    nacos:
      server-addr: 10.211.55.3:8848
      namespace: public
      service-name: iot-gateway-nodes
      group: DEFAULT_GROUP
    zookeeper:
      connect-string: 127.0.0.1:2181
      root-path: /iot/gateway/nodes
    etcd:
      endpoints:
        - http://127.0.0.1:2379
      root-prefix: /iot/gateway/nodes/
      lease-ttl: 20s
```

Nacos uses ephemeral Naming instances, ZooKeeper uses ephemeral ZNodes, and etcd binds node keys
to leases. Enabling `iot.cluster` without a selected and instantiated `NodeRegistry` fails startup.
The test gateway exposes the current membership at `/api/iot/gateway/cluster`.

These modules implement node registration only. Dynamic configuration is handled by the separate
configuration-center SPI described below.

## Distributed configuration center

`magic-api-plugin-configuration-center` defines framework-neutral `get`, prefix `list`, `watch`,
revision and CAS operations. Select one Provider independently from the node registry:

```yaml
iot:
  providers:
    configuration-center:
      type: nacos # nacos | zookeeper | etcd
  configuration-center:
    nacos:
      server-addr: 10.211.55.3:8848
      data-id: iot-gateway-config.json
      group: DEFAULT_GROUP
    zookeeper:
      connect-string: 127.0.0.1:2181
      root-path: /iot/gateway/config
    etcd:
      endpoints:
        - http://127.0.0.1:2379
      root-prefix: /iot/gateway/config/
```

Nacos stores logical keys in one CAS-protected JSON document so prefix listing remains available.
ZooKeeper and etcd store one backend key per logical key. Revisions are opaque tokens prefixed by
`nacos:`, `zookeeper:` or `etcd:`; clients must return them unchanged to CAS or delete operations.
The test gateway exposes list/get/put/CAS/delete under `/api/iot/gateway/configuration`.

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

## MQTT transport

`magic-api-plugin-transport-mqtt` embeds a mica-mqtt broker. MQTT publish packets are converted
directly into unified `DeviceMessage` instances, preserving Topic, QoS, retained and duplicate
metadata. The initial Topic convention is:

```text
devices/{productId}/{deviceId}/properties
devices/{productId}/{deviceId}/events/{eventId}
devices/{productId}/{deviceId}/commands/reply
devices/{productId}/{deviceId}/heartbeat
```

```yaml
iot:
  transports:
    mqtt:
      enabled: true
      host: 0.0.0.0
      port: 1883
      authentication-required: true
      credential-type: secret
      downlink-topic: devices/{productId}/{deviceId}/commands
```

When authentication is enabled, the MQTT username (or client ID when username is empty) uses
`productId/deviceId` and the password is checked by the selected `DeviceRegistry`. Anonymous mode
is intended only for local development and protocol testing; `prod` and `production` profiles
reject startup when MQTT authentication is disabled.

### External MQTT/EMQX transport

`magic-api-plugin-transport-mqtt-client` connects the gateway to an external MQTT broker such as
EMQX. It is separate from the embedded broker provider, supports shared subscriptions, reconnects
and subscribes again after connection recovery, validates Topic identities against the selected
Device Registry, and publishes gateway commands back through the broker.

```yaml
iot:
  transports:
    mqtt-client:
      enabled: true
      server-uri: ssl://emqx.example.com:8883
      client-id-prefix: iot-gateway
      node-id: gateway-node-01
      username: ${EMQX_USERNAME}
      password: ${EMQX_PASSWORD}
      validate-device: true
      subscriptions:
        - topic: $share/iot-gateway/devices/+/+/properties
          qos: 1
        - topic: $share/iot-gateway/devices/+/+/events/+
          qos: 1
        - topic: $share/iot-gateway/devices/+/+/commands/reply
          qos: 1
```

Each gateway node must use a unique `node-id`. Shared subscriptions let EMQX deliver one uplink
message to one gateway node in the group. The synthetic connection ID is
`productId/deviceId`, and downlink commands are published to
`devices/{productId}/{deviceId}/commands`. `ssl://` uses the JVM trust store; custom trust stores
can be supplied with the standard `javax.net.ssl.trustStore` JVM properties.

The first message seen for a device creates a virtual gateway session. This is a routing session,
not authoritative proof that the robot is currently online. Production online/offline state
should be fed from EMQX client lifecycle events into the Redis session provider.

## Modbus TCP

`magic-api-plugin-protocol-modbus-tcp` uses DigitalPetri Modbus for typed PDU encoding and decoding.
The Netty TCP provider has a dedicated MBAP length-field listener, so fragmented or coalesced TCP
packets are not treated as Modbus frames. Supported command function codes are `0x01`, `0x02`,
`0x03`, `0x04`, `0x05`, `0x06`, `0x0F` and `0x10`; exception responses and transaction-to-command
correlation are included.

```yaml
iot:
  transports:
    modbus-tcp:
      enabled: true
      host: 0.0.0.0
      port: 1502
      protocol-role: server
  protocols:
    modbus-tcp:
      enabled: true
```

`protocol-role: server` decodes incoming frames as requests. Use `client` for a future outbound
connector where incoming frames are responses.

External Redis and Kafka integration tests are opt-in:

```bash
mvn test -Diot.integration.enabled=true \
  -Diot.redis.host=10.211.55.4 -Diot.redis.port=6379 \
  -Diot.kafka.bootstrap=10.211.55.4:9092
```

External etcd node-registry and configuration-center integration tests are also opt-in. The test
uses isolated random prefixes and removes its keys and leases when complete:

```bash
mvn -pl providers/magic-api-plugin-node-registry-etcd,providers/magic-api-plugin-configuration-center-etcd \
  -am test \
  -Diot.integration.enabled=true \
  -Diot.etcd.endpoints=http://10.211.55.4:2379,http://10.211.55.4:22379,http://10.211.55.4:32379
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
