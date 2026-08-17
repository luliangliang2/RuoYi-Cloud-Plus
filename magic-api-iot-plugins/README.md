# Magic API IoT Plugins

Independent IoT plugin suite for the adjacent `magic-api` project. This reactor deliberately does not modify or require changes inside `magic-api`.

## Modules

- P0: core, registry, session, protocol, command, message bus, observability
- P1: device shadow, product model, rule engine, OTA, time-series routing
- P2: protocol adapter modules, beginning with `protocol-sample`

## Build

```bash
mvn test
```

All extension modules depend on `magic-api-plugin-iot-core`; the core module has no dependency on transport, storage, Spring, or Magic API runtime code.
