package org.ssssssss.magicapi.iot.protocol;

import org.ssssssss.magicapi.iot.core.model.ProtocolContext;
import org.ssssssss.magicapi.iot.core.spi.*;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProtocolPipelineRegistry {
    private final Map<String, ProtocolPipeline> pipelines;

    public ProtocolPipelineRegistry(Collection<ProtocolDetector> detectors,
                                    Collection<FrameDecoder> frameDecoders,
                                    Collection<MessageDecoder> messageDecoders,
                                    Collection<CommandEncoder> commandEncoders) {
        Map<String, ProtocolDetector> detectorMap = unique(detectors, "detector");
        Map<String, FrameDecoder> frameMap = unique(frameDecoders, "frame decoder");
        Map<String, MessageDecoder> messageMap = unique(messageDecoders, "message decoder");
        Map<String, CommandEncoder> commandMap = unique(commandEncoders, "command encoder");
        Set<String> granularIds = new HashSet<>();
        granularIds.addAll(detectorMap.keySet());
        granularIds.addAll(frameMap.keySet());
        granularIds.addAll(messageMap.keySet());
        granularIds.addAll(commandMap.keySet());

        Map<String, ProtocolPipeline> result = new HashMap<>();
        for (String id : granularIds) {
            result.put(id, new ProtocolPipeline(id,
                required(detectorMap, id, "detector"), required(frameMap, id, "frame decoder"),
                required(messageMap, id, "message decoder"), required(commandMap, id, "command encoder")));
        }
        pipelines = Map.copyOf(result);
    }

    public Optional<ProtocolPipeline> find(String protocolId) {
        return Optional.ofNullable(pipelines.get(protocolId));
    }

    public Optional<ProtocolPipeline> detect(ByteBuffer input, ProtocolContext context) {
        return pipelines.values().stream()
            .sorted(Comparator.comparingInt((ProtocolPipeline pipeline) -> pipeline.detector().priority()).reversed()
                .thenComparing(ProtocolPipeline::protocolId))
            .filter(pipeline -> pipeline.supports(input, context))
            .findFirst();
    }

    public Set<String> protocolIds() {
        return pipelines.keySet();
    }

    private static <T extends ProtocolExtension> Map<String, T> unique(Collection<T> extensions, String type) {
        try {
            return extensions.stream().collect(Collectors.toUnmodifiableMap(ProtocolExtension::protocolId, Function.identity()));
        } catch (IllegalStateException exception) {
            throw new IllegalArgumentException("Duplicate protocol " + type, exception);
        }
    }

    private static <T> T required(Map<String, T> values, String protocolId, String type) {
        T value = values.get(protocolId);
        if (value == null) throw new IllegalArgumentException("Protocol " + protocolId + " is missing " + type);
        return value;
    }
}
