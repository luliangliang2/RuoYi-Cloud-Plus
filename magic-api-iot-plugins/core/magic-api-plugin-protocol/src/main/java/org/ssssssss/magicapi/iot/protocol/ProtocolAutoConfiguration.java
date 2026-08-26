package org.ssssssss.magicapi.iot.protocol;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.util.List;

@AutoConfiguration
public class ProtocolAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    ProtocolPipelineRegistry protocolPipelineRegistry(List<ProtocolDetector> detectors,
        List<FrameDecoder> frameDecoders, List<MessageDecoder> messageDecoders,
        List<CommandEncoder> commandEncoders) {
        return new ProtocolPipelineRegistry(detectors, frameDecoders, messageDecoders, commandEncoders);
    }
}
