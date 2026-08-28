# RuoYi IoT Plugin Test

Build and install the independent plugin reactor first:

```bash
cd ../magic-api-iot-plugins
mvn install -DskipTests
```

Start:

```bash
cd ../iot-plugin/ruoyi-iot-plugin-test
mvn spring-boot:run
```

The default listeners are Raw TCP `19000`, MQTT `1883` and Modbus TCP `1502`. Override them with
`IOT_TCP_PORT`, `IOT_MQTT_PORT` and `IOT_MODBUS_TCP_PORT`. MQTT authentication is disabled only in
this local test configuration; set `IOT_MQTT_AUTHENTICATION_REQUIRED=true` to exercise registry-backed authentication.

Endpoints: `GET /api/iot/gateway/status`, `GET /api/iot/gateway/runtime`,
`GET /api/iot/gateway/providers`, `GET /actuator/health`.
Nacos defaults to 10.211.55.3:8848 and can be overridden with NACOS_SERVER_ADDR, NACOS_USERNAME, NACOS_PASSWORD, NACOS_NAMESPACE. Set NACOS_CONFIG_ENABLED=false for local startup without Nacos.
