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

Endpoints: GET /api/iot/gateway/status, GET /actuator/health.
Nacos defaults to 10.211.55.3:8848 and can be overridden with NACOS_SERVER_ADDR, NACOS_USERNAME, NACOS_PASSWORD, NACOS_NAMESPACE. Set NACOS_CONFIG_ENABLED=false for local startup without Nacos.

