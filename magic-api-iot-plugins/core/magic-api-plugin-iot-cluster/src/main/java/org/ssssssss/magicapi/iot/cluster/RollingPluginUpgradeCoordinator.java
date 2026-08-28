package org.ssssssss.magicapi.iot.cluster;

import org.ssssssss.magicapi.iot.core.spi.NodeRegistry;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/** Coordinates one-node-at-a-time plugin upgrades; node operations are supplied by the gateway runtime. */
public final class RollingPluginUpgradeCoordinator {
    private final NodeRegistry nodes;
    private final NodeUpgradeOperator operator;

    public RollingPluginUpgradeCoordinator(NodeRegistry nodes, NodeUpgradeOperator operator) {
        this.nodes = Objects.requireNonNull(nodes, "nodes");
        this.operator = Objects.requireNonNull(operator, "operator");
    }

    public synchronized RolloutResult upgrade(String pluginId, Path jar, Instant now) {
        Objects.requireNonNull(pluginId, "pluginId");
        Collection<NodeRegistry.GatewayNode> active = nodes.activeNodes(now == null ? Instant.now() : now);
        ArrayList<NodeResult> results = new ArrayList<>();
        for (NodeRegistry.GatewayNode node : active) {
            try {
                operator.drain(node);
                operator.upgrade(node, pluginId, jar);
                operator.awaitReady(node);
                operator.resume(node);
                results.add(new NodeResult(node.nodeId(), State.READY, ""));
            } catch (Exception failure) {
                try { operator.rollback(node, pluginId); } catch (Exception rollbackFailure) { failure.addSuppressed(rollbackFailure); }
                try { operator.resume(node); } catch (Exception resumeFailure) { failure.addSuppressed(resumeFailure); }
                results.add(new NodeResult(node.nodeId(), State.ROLLED_BACK, message(failure)));
                return new RolloutResult(pluginId, false, results);
            }
        }
        return new RolloutResult(pluginId, true, results);
    }

    private static String message(Exception exception) {
        return exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
    }

    public enum State { DRAINING, UPGRADING, READY, ROLLED_BACK }
    public record NodeResult(String nodeId, State state, String error) { }
    public record RolloutResult(String pluginId, boolean successful, Collection<NodeResult> nodes) {
        public RolloutResult { nodes = List.copyOf(nodes); }
    }

    public interface NodeUpgradeOperator {
        void drain(NodeRegistry.GatewayNode node) throws Exception;
        void upgrade(NodeRegistry.GatewayNode node, String pluginId, Path jar) throws Exception;
        void awaitReady(NodeRegistry.GatewayNode node) throws Exception;
        void resume(NodeRegistry.GatewayNode node) throws Exception;
        void rollback(NodeRegistry.GatewayNode node, String pluginId) throws Exception;
    }
}
