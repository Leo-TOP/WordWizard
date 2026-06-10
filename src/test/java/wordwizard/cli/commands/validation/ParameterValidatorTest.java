package wordwizard.cli.commands.validation;

import org.junit.jupiter.api.Test;
import wordwizard.cli.commands.dto.CommandContext;
import wordwizard.exceptions.CommandValidationException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParameterValidatorTest {

    private static CommandContext ctx(List<String> args, Map<String, String> options) {
        return new CommandContext(args, options);
    }

    @Test
    void requireArgCountAcceptsExactAndRejectsOthers() {
        assertDoesNotThrow(() -> ParameterValidator.requireArgCount(ctx(List.of("a"), Map.of()), 1));
        assertThrows(CommandValidationException.class,
                () -> ParameterValidator.requireArgCount(ctx(List.of("a", "b"), Map.of()), 1));
    }

    @Test
    void requireMinArgsChecksLowerBound() {
        assertDoesNotThrow(() -> ParameterValidator.requireMinArgs(ctx(List.of("a", "b"), Map.of()), 2));
        assertThrows(CommandValidationException.class,
                () -> ParameterValidator.requireMinArgs(ctx(List.of(), Map.of()), 1));
    }

    @Test
    void requirePairedArgsRejectsOddCount() {
        assertDoesNotThrow(() -> ParameterValidator.requirePairedArgs(ctx(List.of("a", "b"), Map.of())));
        assertThrows(CommandValidationException.class,
                () -> ParameterValidator.requirePairedArgs(ctx(List.of("a", "b", "c"), Map.of())));
    }

    @Test
    void parseAndValidateLimitHandlesAllCases() {
        assertNull(ParameterValidator.parseAndValidateLimit(null));
        assertEquals(5, ParameterValidator.parseAndValidateLimit("5"));
        assertThrows(CommandValidationException.class,
                () -> ParameterValidator.parseAndValidateLimit("0"));
        assertThrows(CommandValidationException.class,
                () -> ParameterValidator.parseAndValidateLimit("-3"));
        assertThrows(CommandValidationException.class,
                () -> ParameterValidator.parseAndValidateLimit("five"));
    }

    @Test
    void requireOptionChecksPresence() {
        assertDoesNotThrow(() -> ParameterValidator.requireOption(ctx(List.of(), Map.of("output", "f.md")), "output"));
        CommandValidationException e = assertThrows(CommandValidationException.class,
                () -> ParameterValidator.requireOption(ctx(List.of(), Map.of()), "output"));
        assertTrue(e.getMessage().contains("--output"));
    }
}
