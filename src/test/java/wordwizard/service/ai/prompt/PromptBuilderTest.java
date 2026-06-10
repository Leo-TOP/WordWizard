package wordwizard.service.ai.prompt;

import org.junit.jupiter.api.Test;
import wordwizard.models.Definition;
import wordwizard.models.Theme;
import wordwizard.models.Word;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromptBuilderTest {

    private final PromptObjectFormatter formatter = new PromptObjectFormatter();
    private final PromptBuilder builder = new PromptBuilder(formatter);

    private static Word wordWithDefs() {
        return Word.createWordWithoutId("cat", List.of(
                Definition.createWithoutId("a small animal", "noun", "test", null),
                Definition.createWithoutId("to whip", null, "test", null)));
    }

    @Test
    void buildsPromptContainingThemesAndIndexedDefinitions() {
        String prompt = builder.buildThemeAssignmentPrompt(
                List.of(wordWithDefs()),
                List.of(new Theme(1L, "animals", 3)));

        assertTrue(prompt.contains("- animals"));
        assertTrue(prompt.contains("Word: \"cat\""));
        assertTrue(prompt.contains("[0] a small animal (noun)"));
        assertTrue(prompt.contains("[1] to whip"));
        assertTrue(prompt.contains("theme_assignments"));
    }

    @Test
    void emptyThemeListProducesPlaceholder() {
        String prompt = builder.buildThemeAssignmentPrompt(List.of(wordWithDefs()), List.of());
        assertTrue(prompt.contains("No existing themes yet"));
    }

    @Test
    void formatterSkipsBlankThemeNames() {
        String formatted = formatter.formatExistingThemes(List.of(
                new Theme(1L, "animals", 1), new Theme(2L, "  ", 0), new Theme(3L, null, 0)));
        assertEquals("- animals", formatted);
    }
}
