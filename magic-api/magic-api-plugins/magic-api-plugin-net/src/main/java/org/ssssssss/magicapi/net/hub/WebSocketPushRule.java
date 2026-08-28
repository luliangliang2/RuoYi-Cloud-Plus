package org.ssssssss.magicapi.net.hub;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket Hub 通用推送规则。
 */
public class WebSocketPushRule {

    private final Map<String, Object> rules;

    public WebSocketPushRule(Map<String, Object> rules) {
        this.rules = rules == null ? Collections.emptyMap() : new HashMap<>(rules);
    }

    public static WebSocketPushRule from(Map<String, Object> rules) {
        return new WebSocketPushRule(rules);
    }

    public Map<String, Object> getRules() {
        return Collections.unmodifiableMap(rules);
    }

    public String getString(String key) {
        Object value = rules.get(key);
        return value == null ? null : String.valueOf(value);
    }

    public boolean isEmpty() {
        return rules.isEmpty();
    }
}
