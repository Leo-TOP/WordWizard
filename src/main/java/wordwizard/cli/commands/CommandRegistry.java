package wordwizard.cli.commands;

import org.springframework.stereotype.Component;
import wordwizard.exceptions.CommandNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandRegistry {
    private final Map<String, Command> commandMap;

    public CommandRegistry(List<Command> commands) {
        commandMap = commands.stream()
                .collect(Collectors.toUnmodifiableMap(Command::commandName, e -> e));

    }

    public Command getCommand(String commandName) {
        Command command = commandMap.get(commandName);
        if (command == null) {
            throw new CommandNotFoundException(
                    "Unsupported command: \"" + commandName + "\". Available: " + commandMap.keySet());
        }

        return command;
    }
}

