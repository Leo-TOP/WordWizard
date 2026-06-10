package wordwizard.service.ai.response;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import wordwizard.exceptions.AiResponseParseException;
import wordwizard.service.ai.response.dto.BatchThemeResponse;

import static org.junit.jupiter.api.Assertions.*;

class AIResponseProcessorTest {

    private final AIResponseProcessor processor = new AIResponseProcessor(new Gson());

    @Test
    void parsesValidBatchResponse() {
        BatchThemeResponse response = processor.parseBatchResponse("""
                {"theme_assignments":[{"word":"cat","definition_index":1,"theme":"animals"}]}""");

        assertEquals(1, response.themeAssignments().size());
        assertEquals("cat", response.themeAssignments().getFirst().word());
        assertEquals(1, response.themeAssignments().getFirst().definitionIndex());
        assertEquals("animals", response.themeAssignments().getFirst().theme());
    }

    @Test
    void invalidJsonThrowsParseException() {
        AiResponseParseException e = assertThrows(AiResponseParseException.class,
                () -> processor.parseBatchResponse("{{{ definitely not json"));
        assertTrue(e.getMessage().contains("invalid JSON"));
    }

    @Test
    void blankResponseThrowsParseException() {
        assertThrows(AiResponseParseException.class, () -> processor.parseBatchResponse("   "));
    }

    @Test
    void longInvalidResponseIsTruncatedInMessage() {
        String longGarbage = "{nope".repeat(100);
        AiResponseParseException e = assertThrows(AiResponseParseException.class,
                () -> processor.parseBatchResponse(longGarbage));
        assertTrue(e.getMessage().length() < longGarbage.length());
        assertTrue(e.getMessage().contains("…"));
    }
}
