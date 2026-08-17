package org.ssssssss.magicapi.iot.command;

import org.ssssssss.magicapi.iot.core.model.DeviceCommand;
import org.ssssssss.magicapi.iot.core.model.DeviceCommandStatus;
import org.ssssssss.magicapi.iot.core.spi.CommandGateway;
import org.ssssssss.magicapi.iot.core.spi.CommandResult;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCommandGateway implements CommandGateway {

    private final ConcurrentHashMap<String, CommandResult> commands = new ConcurrentHashMap<>();

    @Override
    public CommandResult submit(DeviceCommand command) {
        CommandResult created = new CommandResult(command.commandId(), DeviceCommandStatus.CREATED, 0, null, Instant.now());
        return commands.computeIfAbsent(command.commandId(), ignored -> created);
    }

    @Override
    public Optional<CommandResult> find(String commandId) {
        return Optional.ofNullable(commands.get(commandId));
    }

    @Override
    public CommandResult acknowledge(String commandId, DeviceCommandStatus status, String detail) {
        return commands.compute(commandId, (ignored, current) -> {
            if (current == null) throw new IllegalArgumentException("Unknown command: " + commandId);
            if (current.status().terminal()) return current;
            return new CommandResult(commandId, status, current.attempt(), detail, Instant.now());
        });
    }
}

