package org.ssssssss.magicapi.iot.plugin.runtime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ssssssss.magicapi.iot.plugin.api.PluginService;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExternalPluginManagerTest {
    @TempDir Path temp;

    @Test void discoversInvokesDisablesAndReloadsExternalJar() throws Exception {
        Path plugins = Files.createDirectories(temp.resolve("plugins"));
        Path jar = createPluginJar(plugins.resolve("external-echo.jar"));
        var pluginRegistry = new DefaultPluginRegistry();
        var capabilities = new DefaultCapabilityRegistry();
        var services = new DefaultPluginServiceRegistry();
        try (var manager = new ExternalPluginManager(plugins, temp.resolve("data"), pluginRegistry,
            capabilities, services, getClass().getClassLoader(), Map.of())) {
            manager.start();
            assertEquals(1, manager.snapshots().size());
            assertTrue(pluginRegistry.find("external-echo").isPresent());
            assertEquals("echo-v1", services.services(PluginService.class).get(0).serviceId());
            assertEquals("echo-v1", services.invoke(PluginService.class, "echo-v1", PluginService::serviceId));
            assertEquals(1, services.snapshots().get(0).successes());

            manager.disable("external-echo");
            assertFalse(pluginRegistry.find("external-echo").isPresent());
            assertTrue(services.snapshots().isEmpty());

            manager.enable(jar);
            assertTrue(pluginRegistry.find("external-echo").isPresent());
            assertEquals(1, manager.snapshots().size());
        }
    }

    @Test void rejectsSharedApiAndKeepsDiscoveryErrors() throws Exception {
        Path plugins = Files.createDirectories(temp.resolve("plugins"));
        Path invalid = plugins.resolve("invalid.jar");
        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(invalid))) {
            add(output, "org/ssssssss/magicapi/iot/plugin/api/PluginService.class", "invalid" );
            add(output, "META-INF/iot-plugin.json", "{\"id\":\"invalid\",\"name\":\"Invalid\",\"version\":\"1\",\"apiVersion\":\"1\"}");
        }
        Path valid = createPluginJar(plugins.resolve("valid.jar"));
        try (var manager = new ExternalPluginManager(plugins, temp.resolve("data"),
            new DefaultPluginRegistry(), new DefaultCapabilityRegistry(), new DefaultPluginServiceRegistry(),
            getClass().getClassLoader(), Map.of())) {
            manager.start();
            assertEquals(1, manager.snapshots().size());
            assertEquals("external-echo", manager.snapshots().get(0).pluginId());
            assertTrue(manager.discoveryErrors().keySet().stream().anyMatch(path -> path.endsWith("invalid.jar")));
        }
    }

    @Test void rejectsDuplicatePluginId() throws Exception {
        Path plugins = Files.createDirectories(temp.resolve("plugins"));
        createPluginJar(plugins.resolve("one.jar"));
        createPluginJar(plugins.resolve("two.jar"));
        try (var manager = new ExternalPluginManager(plugins, temp.resolve("data"),
            new DefaultPluginRegistry(), new DefaultCapabilityRegistry(), new DefaultPluginServiceRegistry(),
            getClass().getClassLoader(), Map.of())) {
            manager.start();
            assertEquals(1, manager.snapshots().size());
            assertEquals(1, manager.discoveryErrors().size());
        }
    }

    private Path createPluginJar(Path jar) throws IOException {
        Path sourceRoot = Files.createDirectories(temp.resolve("source"));
        Path classes = Files.createDirectories(temp.resolve("classes"));
        Path source = sourceRoot.resolve("ExternalEchoService.java");
        Files.writeString(source, """
            package external;
            import org.ssssssss.magicapi.iot.plugin.api.PluginService;
            public final class ExternalEchoService implements PluginService {
                public String serviceId() { return "echo-v1"; }
            }
            """, StandardCharsets.UTF_8);
        int result = ToolProvider.getSystemJavaCompiler().run(null, null, null,
            "-classpath", System.getProperty("java.class.path"), "-d", classes.toString(), source.toString());
        if (result != 0) throw new IllegalStateException("Test plugin compilation failed: " + result);

        try (JarOutputStream output = new JarOutputStream(Files.newOutputStream(jar))) {
            add(output, classes.resolve("external/ExternalEchoService.class"), "external/ExternalEchoService.class");
            add(output, "META-INF/services/org.ssssssss.magicapi.iot.plugin.api.PluginService",
                "external.ExternalEchoService\n");
            add(output, "META-INF/iot-plugin.json", """
                {"id":"external-echo","name":"External Echo","version":"1.0.0","apiVersion":"1",
                 "provider":"external","capabilities":["debug:echo"],"requires":[],"optionalRequires":[],
                 "loadOrder":100,"configPrefix":"plugins.external-echo","failurePolicy":"OPTIONAL"}
                """);
        }
        return jar;
    }

    private static void add(JarOutputStream output, Path file, String name) throws IOException {
        output.putNextEntry(new JarEntry(name));
        Files.copy(file, output);
        output.closeEntry();
    }

    private static void add(JarOutputStream output, String name, String value) throws IOException {
        output.putNextEntry(new JarEntry(name));
        output.write(value.getBytes(StandardCharsets.UTF_8));
        output.closeEntry();
    }
}
