package wordwizard.cli.commands;

import org.junit.jupiter.api.Test;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.exceptions.CommandNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandRegistryTest {

    private record FakeCommand(String name) implements Command {
        @Override public String commandName() { return name; }
        @Override public void execute(CommandContext commandContext) {}
    }

    private final CommandRegistry registry =
            new CommandRegistry(List.of(new FakeCommand("help"), new FakeCommand("exit")));

    @Test
    void returnsRegisteredCommand() {
        assertEquals("help", registry.getCommand("help").commandName());
    }

    @Test
    void unknownCommandThrowsWithAvailableList() {
        CommandNotFoundException e = assertThrows(CommandNotFoundException.class,
                () -> registry.getCommand("nope"));
        assertTrue(e.getMessage().contains("nope"));
        assertTrue(e.getMessage().contains("help"));
    }
}
