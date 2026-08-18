package org.dromara.iot;

import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/iot/gateway")
public class IotGatewayController {
    private final DeviceRegistry deviceRegistry;
    private final SessionRepository sessionRepository;
    private final DeviceMessageBus messageBus;
    private final ApplicationContext applicationContext;

    public IotGatewayController(DeviceRegistry deviceRegistry, SessionRepository sessionRepository,
                                DeviceMessageBus messageBus, ApplicationContext applicationContext) {
        this.deviceRegistry = deviceRegistry;
        this.sessionRepository = sessionRepository;
        this.messageBus = messageBus;
        this.applicationContext = applicationContext;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of("status", "UP",
            "deviceRegistry", deviceRegistry.getClass().getSimpleName(),
            "sessionRepository", sessionRepository.getClass().getSimpleName(),
            "messageBus", messageBus.getClass().getSimpleName());
    }

    @GetMapping("/components")
    public Map<String, Object> components() {
        List<Map<String, String>> items = Arrays.stream(applicationContext.getBeanDefinitionNames())
            .map(name -> {
                Class<?> type = applicationContext.getType(name);
                return type == null ? null : Map.entry(name, type);
            })
            .filter(Objects::nonNull)
            .filter(entry -> entry.getValue().getPackageName().startsWith("org.ssssssss.magicapi.iot"))
            .map(entry -> {
                String[] segments = entry.getValue().getPackageName().split("\\.");
                String module = segments.length > 4 ? segments[4] : "core";
                return Map.of("id", entry.getKey(), "name", entry.getKey(), "module", module,
                    "implementation", entry.getValue().getSimpleName(), "status", "UP");
            })
            .sorted(Comparator.comparing(item -> item.get("module") + item.get("name")))
            .toList();
        return Map.of("count", items.size(), "components", items);
    }
}
