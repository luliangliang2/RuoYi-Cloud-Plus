package org.ssssssss.magicapi.iot.observability;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.TelemetryRecorder;

@AutoConfiguration
public class ObservabilityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(TelemetryRecorder.class)
    TelemetryRecorder telemetryRecorder() {
        return new InMemoryTelemetryRecorder();
    }
}

