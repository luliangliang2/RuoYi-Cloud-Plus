package org.ssssssss.magicapi.iot.testkit;
import org.ssssssss.magicapi.iot.plugin.api.PluginDescriptor; import java.util.Objects;
public final class PluginContractAssertions { private PluginContractAssertions(){} public static void assertValid(PluginDescriptor d){Objects.requireNonNull(d);if(d.id().isBlank()||d.version().isBlank()||d.apiVersion().isBlank())throw new AssertionError("Invalid plugin descriptor: "+d.id());} }
