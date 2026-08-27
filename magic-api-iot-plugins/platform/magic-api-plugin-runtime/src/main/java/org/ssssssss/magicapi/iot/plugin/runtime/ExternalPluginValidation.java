package org.ssssssss.magicapi.iot.plugin.runtime;

import java.util.List;

public record ExternalPluginValidation(
    String pluginId,
    String version,
    String jar,
    boolean dependenciesSatisfied,
    boolean duplicatePluginId,
    List<String> missingDependencies,
    List<String> services
) {
    public ExternalPluginValidation {
        missingDependencies = missingDependencies == null ? List.of() : List.copyOf(missingDependencies);
        services = services == null ? List.of() : List.copyOf(services);
    }

    public boolean validForNewInstall() {
        return dependenciesSatisfied && !duplicatePluginId;
    }
}
