# Magic API IoT Plugins

Independent IoT plugin suite for the adjacent `magic-api` project. This reactor deliberately does not modify or require changes inside `magic-api`.

## Modules

- Platform: plugin API, runtime and Spring Boot starter
- P0: core, registry, session, protocol, command, message bus, observability
- P1: device shadow, product model, rule engine, OTA, time-series routing
- P2: protocol adapter modules, beginning with `protocol-sample`

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
