package org.dromara.iot;

import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.core.spi.ObservableTransportProvider;
import org.ssssssss.magicapi.iot.protocol.ProtocolIngressRuntime;
import org.ssssssss.magicapi.iot.plugin.runtime.CapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginSnapshot;
import org.ssssssss.magicapi.iot.plugin.spring.ProviderHealthCatalog;
import org.ssssssss.magicapi.iot.cluster.GatewayNodeCoordinator;
import org.ssssssss.magicapi.iot.config.ConfigurationCenter;
import org.ssssssss.magicapi.iot.config.ConfigurationRuntime;
import org.ssssssss.magicapi.iot.config.nacos.NacosConfigurationCenterProperties;
import org.ssssssss.magicapi.iot.config.zookeeper.ZookeeperConfigurationCenterProperties;
import org.ssssssss.magicapi.iot.config.etcd.EtcdConfigurationCenterProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/iot/gateway")
public class IotGatewayController {
    private static final Set<String> PROVIDER_TYPES = Set.of("device-registry", "device-session", "message-bus",
        "node-registry", "configuration-center", "transport");

    private final DeviceRegistry deviceRegistry;
    private final SessionRepository sessionRepository;
    private final DeviceMessageBus messageBus;
    private final PluginRegistry pluginRegistry;
    private final CapabilityRegistry capabilityRegistry;
    private final ProviderHealthCatalog providerHealthCatalog;
    private final ProtocolIngressRuntime protocolRuntime;
    private final List<ObservableTransportProvider> transports;
    private final GatewayNodeCoordinator clusterCoordinator;
    private final ConfigurationRuntime configurationRuntime;
    private final ObjectProvider<NacosConfigurationCenterProperties> nacosProperties;
    private final ObjectProvider<ZookeeperConfigurationCenterProperties> zookeeperProperties;
    private final ObjectProvider<EtcdConfigurationCenterProperties> etcdProperties;

    public IotGatewayController(DeviceRegistry deviceRegistry, SessionRepository sessionRepository,
                                DeviceMessageBus messageBus, PluginRegistry pluginRegistry,
                                CapabilityRegistry capabilityRegistry, ProviderHealthCatalog providerHealthCatalog,
                                ProtocolIngressRuntime protocolRuntime, List<ObservableTransportProvider> transports,
                                ObjectProvider<GatewayNodeCoordinator> clusterCoordinator,
                                ConfigurationRuntime configurationRuntime,
                                ObjectProvider<NacosConfigurationCenterProperties> nacosProperties,
                                ObjectProvider<ZookeeperConfigurationCenterProperties> zookeeperProperties,
                                ObjectProvider<EtcdConfigurationCenterProperties> etcdProperties) {
        this.deviceRegistry = deviceRegistry;
        this.sessionRepository = sessionRepository;
        this.messageBus = messageBus;
        this.pluginRegistry = pluginRegistry;
        this.capabilityRegistry = capabilityRegistry;
        this.providerHealthCatalog = providerHealthCatalog;
        this.protocolRuntime = protocolRuntime;
        this.transports = List.copyOf(transports);
        this.clusterCoordinator = clusterCoordinator.getIfAvailable();
        this.configurationRuntime = configurationRuntime;
        this.nacosProperties = nacosProperties;
        this.zookeeperProperties = zookeeperProperties;
        this.etcdProperties = etcdProperties;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        var providerHealth = providerHealthCatalog.snapshots();
        boolean providersUp = providerHealth.stream()
            .allMatch(snapshot -> snapshot.health().status() == org.ssssssss.magicapi.iot.plugin.api.PluginHealth.Status.UP);
        return Map.of("status", providersUp ? "UP" : "DOWN",
            "deviceRegistry", deviceRegistry.getClass().getSimpleName(),
            "sessionRepository", sessionRepository.getClass().getSimpleName(),
            "messageBus", messageBus.getClass().getSimpleName(),
            "configurationCenter", configurationRuntime.providerId(),
            "pluginCount", pluginRegistry.snapshots().size(),
            "capabilityCount", capabilityRegistry.capabilities().size(),
            "providerCount", providerHealth.size(),
            "healthyProviderCount", providerHealth.stream().filter(snapshot ->
                snapshot.health().status() == org.ssssssss.magicapi.iot.plugin.api.PluginHealth.Status.UP).count());
    }

    @GetMapping("/providers")
    public Map<String, Object> providers() {
        var providers = providerHealthCatalog.snapshots();
        return Map.of("count", providers.size(), "providers", providers);
    }

    @GetMapping("/runtime")
    public Map<String, Object> runtime() {
        return Map.of(
            "protocol", protocolRuntime.snapshot(),
            "transports", transports.stream().map(ObservableTransportProvider::snapshot).toList());
    }

    @GetMapping("/cluster")
    public Map<String, Object> cluster() {
        if (clusterCoordinator == null) return Map.of("enabled", false, "nodes", List.of());
        return Map.of("enabled", true, "snapshot", clusterCoordinator.snapshot(),
            "nodes", clusterCoordinator.activeNodes());
    }

    @GetMapping("/configuration")
    public Map<String, Object> configuration(@RequestParam(defaultValue = "") String prefix) {
        var values = configurationRuntime.list(prefix);
        return Map.of("provider", configurationRuntime.providerId(), "count", values.size(), "values", values);
    }

    @GetMapping("/configuration/value")
    public Map<String, Object> configurationValue(@RequestParam String key) {
        return Map.of("provider", configurationRuntime.providerId(), "value", configurationRuntime.get(key));
    }

    @PutMapping("/configuration")
    public ConfigurationCenter.ConfigurationValue putConfiguration(@RequestBody ConfigurationWrite request) {
        return configurationRuntime.put(request.key(), request.value());
    }

    @PutMapping("/configuration/cas")
    public ConfigurationCenter.CasResult compareAndSetConfiguration(@RequestBody ConfigurationCasWrite request) {
        return configurationRuntime.compareAndSet(request.key(), request.expectedRevision(), request.value());
    }

    @DeleteMapping("/configuration")
    public ConfigurationCenter.CasResult deleteConfiguration(@RequestParam String key,
                                                              @RequestParam String expectedRevision) {
        return configurationRuntime.delete(key, expectedRevision);
    }

    @GetMapping("/configuration/memory")
    public Map<String, Object> configurationMemory() {
        return Map.of("provider", configurationRuntime.providerId(),
            "count", configurationRuntime.list("").size(),
            "values", configurationRuntime.list(""),
            "parsed", configurationRuntime.parsedSnapshots(),
            "parserErrors", configurationRuntime.parserErrors());
    }

    @GetMapping("/configuration/meta")
    public Map<String, Object> configurationMeta() {
        String provider = configurationRuntime.providerId();
        Map<String, Object> details = switch (provider) {
            case "nacos" -> {
                var p = nacosProperties.getIfAvailable();
                yield p == null ? Map.of() : Map.of("serverAddr", p.getServerAddr(), "namespace", p.getNamespace(),
                    "dataId", p.getDataId(), "group", p.getGroup());
            }
            case "zookeeper" -> {
                var p = zookeeperProperties.getIfAvailable();
                yield p == null ? Map.of() : Map.of("connectString", p.getConnectString(), "rootPath", p.getRootPath());
            }
            case "etcd" -> {
                var p = etcdProperties.getIfAvailable();
                yield p == null ? Map.of() : Map.of("endpoints", p.getEndpoints(), "rootPrefix", p.getRootPrefix());
            }
            default -> Map.of();
        };
        return Map.of("provider", provider, "details", details);
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
        var providerHealth = providerHealthCatalog.snapshots().stream()
            .filter(provider -> descriptor.capabilities().contains(
                provider.providerType() + ":" + provider.providerId()))
            .findFirst();
        boolean providerPlugin = descriptor.capabilities().stream()
            .map(capability -> capability.split(":", 2)[0])
            .anyMatch(PROVIDER_TYPES::contains);
        String health = providerHealth.map(provider -> provider.health().status().name())
            .orElse(snapshot.health().status().name());
        String status = providerHealth.map(provider -> provider.health().status().name())
            .orElse(providerPlugin ? "INACTIVE" : snapshot.state().name());
        return Map.ofEntries(
            Map.entry("id", descriptor.id()),
            Map.entry("name", descriptor.name()),
            Map.entry("module", module),
            Map.entry("implementation", descriptor.provider()),
            Map.entry("status", status),
            Map.entry("health", health),
            Map.entry("version", descriptor.version()),
            Map.entry("apiVersion", descriptor.apiVersion()),
            Map.entry("capabilities", descriptor.capabilities()),
            Map.entry("requires", descriptor.requires()),
            Map.entry("optionalRequires", descriptor.optionalRequires()),
            Map.entry("failurePolicy", descriptor.failurePolicy().name()),
            Map.entry("updatedAt", snapshot.updatedAt().toString()),
            Map.entry("lastError", snapshot.lastError()));
    }

    public record ConfigurationWrite(String key, String value) { }
    public record ConfigurationCasWrite(String key, String value, String expectedRevision) { }
}
