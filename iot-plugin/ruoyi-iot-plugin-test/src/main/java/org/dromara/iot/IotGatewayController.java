package org.dromara.iot;

import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.plugin.runtime.CapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginSnapshot;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/iot/gateway")
public class IotGatewayController {
    private final DeviceRegistry deviceRegistry;
    private final SessionRepository sessionRepository;
    private final DeviceMessageBus messageBus;
    private final PluginRegistry pluginRegistry;
    private final CapabilityRegistry capabilityRegistry;

    public IotGatewayController(DeviceRegistry deviceRegistry, SessionRepository sessionRepository,
                                DeviceMessageBus messageBus, PluginRegistry pluginRegistry,
                                CapabilityRegistry capabilityRegistry) {
        this.deviceRegistry = deviceRegistry;
        this.sessionRepository = sessionRepository;
        this.messageBus = messageBus;
        this.pluginRegistry = pluginRegistry;
        this.capabilityRegistry = capabilityRegistry;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of("status", "UP",
            "deviceRegistry", deviceRegistry.getClass().getSimpleName(),
            "sessionRepository", sessionRepository.getClass().getSimpleName(),
            "messageBus", messageBus.getClass().getSimpleName(),
            "pluginCount", pluginRegistry.snapshots().size(),
            "capabilityCount", capabilityRegistry.capabilities().size());
    }

    @GetMapping("/components")
    public Map<String, Object> components() {
        List<Map<String, Object>> items = pluginRegistry.snapshots().stream()
            .map(this::componentView)
            .toList();
        return Map.of(
            "count", items.size(),
            "capabilityCount", capabilityRegistry.capabilities().size(),
            "capabilities", capabilityRegistry.capabilities(),
            "components", items);
    }

    private Map<String, Object> componentView(PluginSnapshot snapshot) {
        var descriptor = snapshot.descriptor();
        String module = descriptor.capabilities().isEmpty()
            ? "plugin"
            : descriptor.capabilities().get(0).split(":", 2)[0];
        return Map.ofEntries(
            Map.entry("id", descriptor.id()),
            Map.entry("name", descriptor.name()),
            Map.entry("module", module),
            Map.entry("implementation", descriptor.provider()),
            Map.entry("status", snapshot.state().name()),
            Map.entry("health", snapshot.health().status().name()),
            Map.entry("version", descriptor.version()),
            Map.entry("apiVersion", descriptor.apiVersion()),
            Map.entry("capabilities", descriptor.capabilities()),
            Map.entry("requires", descriptor.requires()),
            Map.entry("optionalRequires", descriptor.optionalRequires()),
            Map.entry("failurePolicy", descriptor.failurePolicy().name()),
            Map.entry("updatedAt", snapshot.updatedAt().toString()),
            Map.entry("lastError", snapshot.lastError()));
    }
}
