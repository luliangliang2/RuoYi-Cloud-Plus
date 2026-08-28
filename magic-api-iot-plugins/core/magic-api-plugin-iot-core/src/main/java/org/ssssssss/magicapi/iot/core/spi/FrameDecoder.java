package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.ProtocolContext;

import java.nio.ByteBuffer;
import java.util.List;

public interface FrameDecoder extends ProtocolExtension {
	List<ByteBuffer> decodeFrames(ByteBuffer input, ProtocolContext context);
}
