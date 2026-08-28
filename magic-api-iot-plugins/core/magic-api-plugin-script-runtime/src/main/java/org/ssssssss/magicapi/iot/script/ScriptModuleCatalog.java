package org.ssssssss.magicapi.iot.script;

import java.util.List;
import java.util.Map;

/** The allowlisted module and facade surface exposed to the script editor/runtime. */
public final class ScriptModuleCatalog {
    private ScriptModuleCatalog() { }

    public static List<Module> defaults() {
        return List.of(
                new Module("iot", "IoT 编排 API", List.of("device", "route", "message", "command"), Map.of()),
                new Module("iot.device", "设备上下文与认证动作", List.of("authenticate", "read", "shadow"), Map.of(
                        "authenticate", List.of("productId", "deviceId"), "read", List.of("productId", "deviceId"))),
                new Module("iot.route", "设备路由动作", List.of("bind", "unbind", "resolve"), Map.of(
                        "bind", List.of("productId", "deviceId", "nodeId"), "unbind", List.of("productId", "deviceId"))),
                new Module("iot.message", "消息投递动作", List.of("publish", "reply"), Map.of(
                        "publish", List.of("topic", "payload"), "reply", List.of("payload"))),
                new Module("iot.command", "设备命令动作", List.of("send", "cancel"), Map.of(
                        "send", List.of("productId", "deviceId", "commandId", "parameters"))));
    }

    public record Module(String moduleId, String description, List<String> exports,
                         Map<String, List<String>> parameters) {
        public Module {
            exports = exports == null ? List.of() : List.copyOf(exports);
            parameters = parameters == null ? Map.of() : Map.copyOf(parameters);
        }
    }
}
