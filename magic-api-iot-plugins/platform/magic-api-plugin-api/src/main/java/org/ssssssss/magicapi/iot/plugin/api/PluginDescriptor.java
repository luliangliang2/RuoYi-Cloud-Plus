package org.ssssssss.magicapi.iot.plugin.api;

import java.util.List;

public record PluginDescriptor(
    String id,
    String name,
    String version,
    String apiVersion,
    String provider,
    List<String> capabilities,
    List<String> requires,
    List<String> optionalRequires,
    int loadOrder,
    String configPrefix,
    PluginFailurePolicy failurePolicy
) {
    public PluginDescriptor {
        capabilities = capabilities == null ? List.of() : List.copyOf(capabilities);
        requires = requires == null ? List.of() : List.copyOf(requires);
        optionalRequires = optionalRequires == null ? List.of() : List.copyOf(optionalRequires);
        failurePolicy = failurePolicy == null ? PluginFailurePolicy.OPTIONAL : failurePolicy;
    }
}
