package wordwizard.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wordwizard.cli.commands.Command;
import wordwizard.cli.commands.CommandRegistry;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.cli.parsing.InputParser;
import wordwizard.exceptions.CommandNotFoundException;
import wordwizard.exceptions.exceptionhandling.GlobalExceptionHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CliEngineTest {

    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreStdin() {
        System.setIn(originalIn);
    }

    private static void feedInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void executesParsedCommandsAndStopsAtEof() {
        feedInput("\nget-word cat --verbose\n");
        CommandRegistry registry = Mockito.mock(CommandRegistry.class);
        Command command = Mockito.mock(Command.class);
        when(registry.getCommand("get-word")).thenReturn(command);

        new CliEngine(registry, new GlobalExceptionHandler(), new InputParser()).run();

        verify(command).execute(any(CommandContext.class));
    }

    @Test
    void printsHandledMessageInsteadOfCrashingOnErrors() {
        feedInput("boom\n");
        CommandRegistry registry = Mockito.mock(CommandRegistry.class);
        when(registry.getCommand("boom")).thenThrow(new CommandNotFoundException("no such command"));

        // must not throw: the loop handles the exception and then hits EOF
        new CliEngine(registry, new GlobalExceptionHandler(), new InputParser()).run();

        verify(registry).getCommand("boom");
    }
}
