package wordwizard.cli.parsing;

import org.junit.jupiter.api.Test;
import wordwizard.cli.parsing.dto.ParsedInput;
import wordwizard.exceptions.CommandValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    private final InputParser parser = new InputParser();

    @Test
    void parsesCommandArgsAndOptions() {
        ParsedInput input = parser.parse("get-word Cat --limit 5 --verbose");

        assertEquals("get-word", input.commandName());
        assertEquals(List.of("cat"), input.args());
        assertEquals("5", input.options().get("limit"));
        assertEquals("true", input.options().get("verbose"));
    }

    @Test
    void treatsQuotedTextAsSingleToken() {
        ParsedInput input = parser.parse("add cat \"a small animal\"");

        assertEquals("add", input.commandName());
        assertEquals(List.of("cat", "a small animal"), input.args());
    }

    @Test
    void optionFollowedByOptionIsBoolean() {
        ParsedInput input = parser.parse("filter --verbose --theme animals");

        assertEquals("true", input.options().get("verbose"));
        assertEquals("animals", input.options().get("theme"));
    }

    @Test
    void lowercasesInput() {
        ParsedInput input = parser.parse("GET-WORD CAT");

        assertEquals("get-word", input.commandName());
        assertEquals(List.of("cat"), input.args());
    }

    @Test
    void blankInputThrowsValidationException() {
        assertThrows(CommandValidationException.class, () -> parser.parse("   "));
    }

    @Test
    void commandWithoutArgsParses() {
        ParsedInput input = parser.parse("stats");

        assertEquals("stats", input.commandName());
        assertTrue(input.args().isEmpty());
        assertTrue(input.options().isEmpty());
    }
}
