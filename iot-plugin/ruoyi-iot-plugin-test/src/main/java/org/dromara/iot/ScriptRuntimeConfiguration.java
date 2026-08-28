package org.dromara.iot;

import org.ssssssss.magicapi.iot.script.*;
import org.ssssssss.magicapi.iot.script.aviator.AviatorScriptEngineProvider;
import org.ssssssss.magicapi.iot.script.graalvm.GraalVmScriptEngineProvider;
import org.ssssssss.magicapi.iot.script.groovy.GroovyScriptEngineProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import java.util.List;

@Configuration
public class ScriptRuntimeConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "iot.script.registry", name = "type", havingValue = "memory")
    ScriptRegistry scriptRegistry() { return new InMemoryScriptRegistry(); }

    @Bean
    ScriptEngineProvider aviatorScriptEngineProvider() { return new AviatorScriptEngineProvider(); }

    @Bean
    ScriptEngineProvider graalVmScriptEngineProvider() { return new GraalVmScriptEngineProvider(); }

    @Bean
    ScriptEngineProvider groovyScriptEngineProvider() { return new GroovyScriptEngineProvider(); }

    @Bean
    ScriptRuntime scriptRuntime(ScriptRegistry registry, List<ScriptEngineProvider> providers) {
        return new ScriptRuntime(registry, providers);
    }
}
