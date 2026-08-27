package org.dromara.iot;

import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistry;
import org.ssssssss.magicapi.iot.core.spi.DeviceRegistryAdmin;
import org.ssssssss.magicapi.iot.core.spi.DeviceCredential;
import org.ssssssss.magicapi.iot.core.spi.RegisteredDevice;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.spi.SessionRepository;
import org.ssssssss.magicapi.iot.core.spi.ObservableTransportProvider;
import org.ssssssss.magicapi.iot.protocol.ProtocolIngressRuntime;
import org.ssssssss.magicapi.iot.plugin.runtime.CapabilityRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginRegistry;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginSnapshot;
import org.ssssssss.magicapi.iot.plugin.runtime.ExternalPluginManager;
import org.ssssssss.magicapi.iot.plugin.runtime.PluginServiceRegistry;
import org.ssssssss.magicapi.iot.plugin.spring.ProviderHealthCatalog;
import org.ssssssss.magicapi.iot.plugin.spring.HandshakeDebugService;
import org.ssssssss.magicapi.iot.core.spi.HandshakeProviderRegistry;
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
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api/iot/gateway")
public class IotGatewayController {
    private static final Set<String> PROVIDER_TYPES = Set.of("device-registry", "device-session", "message-bus",
        "node-registry", "configuration-center", "transport");

    private final DeviceRegistry deviceRegistry;
    private final DeviceRegistryAdmin deviceRegistryAdmin;
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
    private final PluginServiceRegistry pluginServices;
    private final HandshakeProviderRegistry handshakeProviders;
    private final HandshakeDebugService handshakeDebug;
    private final ExternalPluginManager externalPlugins;

    public IotGatewayController(DeviceRegistry deviceRegistry, DeviceRegistryAdmin deviceRegistryAdmin,
                                SessionRepository sessionRepository,
                                DeviceMessageBus messageBus, PluginRegistry pluginRegistry,
                                CapabilityRegistry capabilityRegistry, ProviderHealthCatalog providerHealthCatalog,
                                ProtocolIngressRuntime protocolRuntime, List<ObservableTransportProvider> transports,
                                ObjectProvider<GatewayNodeCoordinator> clusterCoordinator,
                                ConfigurationRuntime configurationRuntime,
                                ObjectProvider<NacosConfigurationCenterProperties> nacosProperties,
                                ObjectProvider<ZookeeperConfigurationCenterProperties> zookeeperProperties,
                                ObjectProvider<EtcdConfigurationCenterProperties> etcdProperties,
                                PluginServiceRegistry pluginServices,
                                HandshakeProviderRegistry handshakeProviders,
                                HandshakeDebugService handshakeDebug,
                                ObjectProvider<ExternalPluginManager> externalPlugins) {
        this.deviceRegistry = deviceRegistry;
        this.deviceRegistryAdmin = deviceRegistryAdmin;
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
        this.pluginServices = pluginServices;
        this.handshakeProviders = handshakeProviders;
        this.handshakeDebug = handshakeDebug;
        this.externalPlugins = externalPlugins.getIfAvailable();
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

    @GetMapping("/devices")
    public DeviceRegistryAdmin.DevicePage devices(@RequestParam(defaultValue = "") String productId,
                                                   @RequestParam(defaultValue = "") String keyword,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "20") int pageSize) {
        return deviceRegistryAdmin.search(productId, keyword, page, pageSize);
    }

    @GetMapping("/devices/{productId}/{deviceId}")
    public RegisteredDevice device(@PathVariable String productId, @PathVariable String deviceId) {
        return deviceRegistry.find(new DeviceIdentity(productId, deviceId))
            .orElseThrow(() -> new IllegalArgumentException("Device is not registered: " + productId + "/" + deviceId));
    }

    @PostMapping("/devices")
    public Map<String, Object> registerDevice(@RequestBody DeviceRegistration request) {
        DeviceIdentity identity = new DeviceIdentity(request.productId(), request.deviceId());
        RegisteredDevice saved = deviceRegistryAdmin.register(new RegisteredDevice(identity, request.enabled(),
            requireText(request.protocolId(), "protocolId"), request.capabilities(), 1L));
        String generatedCredential = configureCredential(identity, request.credentialType(), request.credential(),
            request.generateCredential());
        return Map.of("device", saved, "credentialTypes", deviceRegistryAdmin.credentialTypes(identity),
            "generatedCredential", generatedCredential);
    }

    @PutMapping("/devices/{productId}/{deviceId}")
    public Map<String, Object> updateDevice(@PathVariable String productId, @PathVariable String deviceId,
                                             @RequestBody DeviceUpdate request) {
        DeviceIdentity identity = new DeviceIdentity(productId, deviceId);
        RegisteredDevice current = requireDevice(identity);
        RegisteredDevice updated = deviceRegistryAdmin.update(new RegisteredDevice(identity, request.enabled(),
            requireText(request.protocolId(), "protocolId"), request.capabilities(), current.version() + 1));
        String generatedCredential = configureCredential(identity, request.credentialType(), request.credential(),
            request.generateCredential());
        return Map.of("device", updated, "credentialTypes", deviceRegistryAdmin.credentialTypes(identity),
            "generatedCredential", generatedCredential);
    }

    @DeleteMapping("/devices/{productId}/{deviceId}")
    public void deleteDevice(@PathVariable String productId, @PathVariable String deviceId) {
        deviceRegistryAdmin.delete(new DeviceIdentity(productId, deviceId));
    }

    @PutMapping("/devices/{productId}/{deviceId}/status")
    public RegisteredDevice changeDeviceStatus(@PathVariable String productId, @PathVariable String deviceId,
                                                @RequestBody DeviceStatusChange request) {
        return deviceRegistryAdmin.setEnabled(new DeviceIdentity(productId, deviceId), request.enabled());
    }

    @GetMapping("/devices/{productId}/{deviceId}/credentials")
    public Map<String, Object> deviceCredentials(@PathVariable String productId, @PathVariable String deviceId) {
        DeviceIdentity identity = new DeviceIdentity(productId, deviceId);
        requireDevice(identity);
        return Map.of("productId", productId, "deviceId", deviceId,
            "credentialTypes", deviceRegistryAdmin.credentialTypes(identity));
    }

    @PutMapping("/devices/{productId}/{deviceId}/credentials")
    public Map<String, Object> setDeviceCredential(@PathVariable String productId, @PathVariable String deviceId,
                                                   @RequestBody CredentialWrite request) {
        DeviceIdentity identity = new DeviceIdentity(productId, deviceId);
        requireDevice(identity);
        String generatedCredential = configureCredential(identity, request.type(), request.value(), request.generate());
        return Map.of("credentialTypes", deviceRegistryAdmin.credentialTypes(identity),
            "generatedCredential", generatedCredential);
    }

    @DeleteMapping("/devices/{productId}/{deviceId}/credentials/{credentialType}")
    public void deleteDeviceCredential(@PathVariable String productId, @PathVariable String deviceId,
                                       @PathVariable String credentialType) {
        DeviceIdentity identity = new DeviceIdentity(productId, deviceId);
        requireDevice(identity);
        deviceRegistryAdmin.deleteCredential(identity, requireText(credentialType, "credentialType"));
    }

    @PostMapping("/devices/{productId}/{deviceId}/credentials/verify")
    public Map<String, Object> verifyDeviceCredential(@PathVariable String productId, @PathVariable String deviceId,
                                                       @RequestBody CredentialVerify request) {
        DeviceIdentity identity = new DeviceIdentity(productId, deviceId);
        requireDevice(identity);
        boolean verified = deviceRegistryAdmin.verifyCredential(identity,
            new DeviceCredential(requireText(request.type(), "credentialType"), requireText(request.value(), "credential")));
        return Map.of("verified", verified);
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

    @GetMapping("/spi")
    public Map<String, Object> spi() {
        var services = pluginServices.snapshots();
        return Map.of("count", services.size(), "services", services,
            "handshakeProviders", handshakeProviders.handshakes().stream().map(provider -> Map.of(
                "id", provider.serviceId(), "protocols", provider.supportedProtocols())).toList(),
            "authenticators", handshakeProviders.authenticators().stream().map(authenticator -> Map.of(
                "id", authenticator.serviceId())).toList());
    }

    @GetMapping("/spi/matches")
    public Map<String, Object> spiMatches(@RequestParam String protocolId) {
        var candidates = handshakeProviders.handshakes().stream()
            .filter(provider -> provider.supportedProtocols().contains(protocolId))
            .map(provider -> Map.of("id", provider.serviceId(), "protocols", provider.supportedProtocols()))
            .toList();
        return Map.of("protocolId", protocolId, "count", candidates.size(), "candidates", candidates,
            "conflict", candidates.size() > 1);
    }

    @PostMapping("/spi/handshake/debug")
    public HandshakeDebugService.DebugResult debugHandshake(
        @RequestBody HandshakeDebugService.DebugRequest request) {
        return handshakeDebug.debug(request);
    }

    @GetMapping("/plugins/external")
    public Map<String, Object> externalPlugins() {
        if (externalPlugins == null) return Map.of("enabled", false, "plugins", List.of(), "errors", Map.of());
        return Map.of("enabled", true, "directory", externalPlugins.pluginDirectory().toString(),
            "plugins", externalPlugins.snapshots(), "errors", externalPlugins.discoveryErrors());
    }

    @PostMapping("/plugins/external/rescan")
    public Map<String, Object> rescanExternalPlugins() {
        requireExternalPlugins().rescan();
        return externalPlugins();
    }

    @PostMapping("/plugins/external/enable")
    public Object enableExternalPlugin(@RequestBody ExternalPluginPath request) {
        return requireExternalPlugins().enable(Path.of(request.jar()));
    }

    @PostMapping("/plugins/external/validate")
    public Object validateExternalPlugin(@RequestBody ExternalPluginPath request) {
        return requireExternalPlugins().validate(Path.of(request.jar()));
    }

    @PostMapping("/plugins/external/{pluginId}/disable")
    public void disableExternalPlugin(@PathVariable String pluginId) {
        requireExternalPlugins().disable(pluginId);
    }

    @PostMapping("/plugins/external/{pluginId}/reload")
    public Object reloadExternalPlugin(@PathVariable String pluginId) {
        return requireExternalPlugins().reload(pluginId);
    }

    @PostMapping("/plugins/external/{pluginId}/upgrade")
    public Object upgradeExternalPlugin(@PathVariable String pluginId, @RequestBody ExternalPluginPath request) {
        return requireExternalPlugins().upgrade(pluginId, Path.of(request.jar()));
    }

    @PostMapping("/plugins/external/{pluginId}/rollback")
    public Object rollbackExternalPlugin(@PathVariable String pluginId) {
        return requireExternalPlugins().rollback(pluginId);
    }

    private ExternalPluginManager requireExternalPlugins() {
        if (externalPlugins == null) throw new IllegalStateException("External plugin loading is disabled");
        return externalPlugins;
    }

    private RegisteredDevice requireDevice(DeviceIdentity identity) {
        return deviceRegistry.find(identity)
            .orElseThrow(() -> new IllegalArgumentException("Device is not registered: " + identity.routingKey()));
    }

    private String configureCredential(DeviceIdentity identity, String type, String value, boolean generate) {
        String credentialType = type == null || type.isBlank() ? "secret" : type.trim();
        String credential = generate ? generateCredential() : value;
        if (credential == null || credential.isBlank()) return "";
        deviceRegistryAdmin.setCredential(identity, new DeviceCredential(credentialType, credential));
        return generate ? credential : "";
    }

    private static String generateCredential() {
        byte[] bytes = new byte[24];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return value.trim();
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
    public record ExternalPluginPath(String jar) { }
    public record DeviceRegistration(String productId, String deviceId, boolean enabled, String protocolId,
                                     Map<String, String> capabilities, String credentialType, String credential,
                                     boolean generateCredential) { }
    public record DeviceUpdate(boolean enabled, String protocolId, Map<String, String> capabilities,
                               String credentialType, String credential, boolean generateCredential) { }
    public record DeviceStatusChange(boolean enabled) { }
    public record CredentialWrite(String type, String value, boolean generate) { }
    public record CredentialVerify(String type, String value) { }
}
