package org.ssssssss.magicapi.iot.security;

import java.util.Set;

public record AccessPolicy(String principal, Set<String> allowedActions, Set<String> allowedTopics) {
    public AccessPolicy {
        allowedActions = allowedActions == null ? Set.of() : Set.copyOf(allowedActions);
        allowedTopics = allowedTopics == null ? Set.of() : Set.copyOf(allowedTopics);
    }
    public boolean allowsAction(String action) { return allowedActions.contains("*") || allowedActions.contains(action); }
    public boolean allowsTopic(String topic) { return allowedTopics.contains("*") || allowedTopics.contains(topic); }
}

