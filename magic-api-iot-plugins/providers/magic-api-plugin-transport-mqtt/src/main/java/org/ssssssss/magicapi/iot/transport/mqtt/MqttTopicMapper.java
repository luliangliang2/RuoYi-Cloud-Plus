package org.ssssssss.magicapi.iot.transport.mqtt;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.DeviceMessageType;

final class MqttTopicMapper {
    MappedTopic map(String topic, String clientId) {
        String[] parts = topic == null ? new String[0] : topic.split("/");
        if (parts.length >= 4 && "devices".equals(parts[0])) {
            DeviceIdentity identity = new DeviceIdentity(parts[1], parts[2]);
            return new MappedTopic(identity, messageType(parts), detail(parts));
        }
        return new MappedTopic(identity(clientId), DeviceMessageType.RAW, "");
    }

    DeviceIdentity identity(String value) {
        String[] parts = value == null ? new String[0] : value.split("/", 2);
        return parts.length == 2 ? new DeviceIdentity(parts[0], parts[1])
            : new DeviceIdentity("mqtt", value == null || value.isBlank() ? "unknown" : value);
    }

    private static DeviceMessageType messageType(String[] parts) {
        return switch (parts[3]) {
            case "properties" -> DeviceMessageType.PROPERTY_REPORT;
            case "events" -> DeviceMessageType.EVENT_REPORT;
            case "heartbeat" -> DeviceMessageType.HEARTBEAT;
            case "commands" -> parts.length > 4 && "reply".equals(parts[4])
                ? DeviceMessageType.COMMAND_REPLY : DeviceMessageType.RAW;
            case "firmware" -> DeviceMessageType.FIRMWARE_PROGRESS;
            default -> DeviceMessageType.RAW;
        };
    }

    private static String detail(String[] parts) {
        return parts.length > 4 ? parts[4] : "";
    }

    record MappedTopic(DeviceIdentity device, DeviceMessageType type, String detail) { }
}
