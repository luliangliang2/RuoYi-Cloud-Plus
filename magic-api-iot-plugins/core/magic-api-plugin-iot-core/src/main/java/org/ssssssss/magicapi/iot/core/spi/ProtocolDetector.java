package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.ProtocolContext;

import java.nio.ByteBuffer;

public interface ProtocolDetector extends ProtocolExtension {
    boolean supports(ByteBuffer input, ProtocolContext context);
}
