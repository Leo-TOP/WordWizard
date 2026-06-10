package wordwizard.service.ai.mapping;

import org.junit.jupiter.api.Test;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.service.ai.response.dto.BatchThemeResponse;
import wordwizard.service.ai.response.dto.ThemeAssignment;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIResponseDtoMapperTest {

    private final AIResponseDtoMapper mapper = new AIResponseDtoMapper();

    private static Word word(long id, String text, long defId) {
        return new Word(id, text, LocalDateTime.now(), LocalDateTime.now(),
                List.of(new Definition(defId, text + " definition", "noun", "test", null, null)));
    }

    @Test
    void mapsValidAssignmentCaseInsensitivelyAndCleansTheme() {
        BatchThemeResponse response = new BatchThemeResponse(List.of(
                new ThemeAssignment("CAT", 0, "  Animals ")));

        List<AssignmentInput> inputs = mapper.toAssignments(response, List.of(word(1L, "cat", 10L)));

        assertEquals(1, inputs.size());
        assertEquals(1L, inputs.getFirst().wordId());
        assertEquals(10L, inputs.getFirst().definitionId());
        assertEquals("animals", inputs.getFirst().themeName());
    }

    @Test
    void nullOrEmptyResponseYieldsNoAssignments() {
        assertTrue(mapper.toAssignments(null, List.of()).isEmpty());
        assertTrue(mapper.toAssignments(new BatchThemeResponse(null), List.of()).isEmpty());
        assertTrue(mapper.toAssignments(new BatchThemeResponse(List.of()), List.of()).isEmpty());
    }

    @Test
    void skipsInvalidAssignments() {
        List<Word> words = List.of(word(1L, "cat", 10L));
        BatchThemeResponse response = new BatchThemeResponse(Arrays.asList(
                null,                                          // null entry
                new ThemeAssignment("", 0, "animals"),         // blank word
                new ThemeAssignment("cat", 0, "  "),           // blank theme
                new ThemeAssignment("ghost", 0, "animals"),    // unknown word
                new ThemeAssignment("cat", 99, "animals"),     // index out of bounds
                new ThemeAssignment("cat", -1, "animals")      // negative index
        ));

        assertTrue(mapper.toAssignments(response, words).isEmpty());
    }
}
