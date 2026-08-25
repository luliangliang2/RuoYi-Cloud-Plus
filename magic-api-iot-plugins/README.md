# Magic API IoT Plugins

Independent IoT plugin suite for the adjacent `magic-api` project. This reactor deliberately does not modify or require changes inside `magic-api`.

## Modules

- `platform/`: plugin API, runtime and Spring Boot starter
- `core/`: gateway model/SPI, protocol, command, observability and security
- `features/`: device shadow, product model, rule engine, OTA, time-series, storage, northbound and ops console
- `adapters/`: protocol adapter modules, beginning with `protocol-sample`
- `providers/`: replaceable infrastructure providers, currently development-only memory providers

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
