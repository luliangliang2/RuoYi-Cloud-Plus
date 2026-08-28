package org.ssssssss.magicapi.iot.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ssssssss.magicapi.iot.config.ConfigurationRuntime;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class RuleEngineAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    InMemoryRuleEngine ruleEngine(List<RuleActionProvider> actions) {
        return new InMemoryRuleEngine(actions);
    }

    @Bean
    ConfigurationRuleParser configurationRuleParser(ObjectMapper mapper, ConfigurationRuntime runtime,
                                                     InMemoryRuleEngine engine) {
        ConfigurationRuleParser parser = new ConfigurationRuleParser(mapper, engine);
        runtime.registerParser(parser);
        return parser;
    }
}
