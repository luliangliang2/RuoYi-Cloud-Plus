package org.ssssssss.magicapi.iot.observability;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.TelemetryRecorder;
import io.micrometer.core.instrument.MeterRegistry;

@AutoConfiguration
public class ObservabilityAutoConfiguration {
    @Bean
    @ConditionalOnBean(MeterRegistry.class)
    @ConditionalOnMissingBean(TelemetryRecorder.class)
    TelemetryRecorder micrometerTelemetryRecorder(MeterRegistry registry) {
        return new MicrometerTelemetryRecorder(registry);
    }

    @Bean
    @ConditionalOnMissingBean({MeterRegistry.class, TelemetryRecorder.class})
    TelemetryRecorder telemetryRecorder() {
        return new InMemoryTelemetryRecorder();
    }
}
