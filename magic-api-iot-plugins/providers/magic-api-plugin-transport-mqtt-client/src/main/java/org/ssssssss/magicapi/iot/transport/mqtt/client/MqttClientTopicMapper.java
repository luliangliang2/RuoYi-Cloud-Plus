package org.ssssssss.magicapi.iot.transport.mqtt.client;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;
import org.ssssssss.magicapi.iot.core.model.DeviceMessageType;

final class MqttClientTopicMapper {
    MappedTopic map(String topic) {
        String[] parts = topic == null ? new String[0] : topic.split("/");
        if (parts.length < 4 || !"devices".equals(parts[0])) {
            throw new IllegalArgumentException("Unsupported device MQTT topic: " + topic);
        }
        return new MappedTopic(new DeviceIdentity(parts[1], parts[2]), messageType(parts), detail(parts));
    }

    String downlink(String template, DeviceIdentity device) {
        return template.replace("{productId}", device.productId()).replace("{deviceId}", device.deviceId());
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

    private static String detail(String[] parts) { return parts.length > 4 ? parts[4] : ""; }

    record MappedTopic(DeviceIdentity device, DeviceMessageType type, String detail) { }
}
