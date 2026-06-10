package wordwizard.service.validation;

import org.junit.jupiter.api.Test;
import wordwizard.exceptions.InvalidDefinitionException;
import wordwizard.exceptions.InvalidStartsWithException;
import wordwizard.exceptions.InvalidThemeException;
import wordwizard.exceptions.InvalidWordException;
import wordwizard.service.validation.pos.PartOfSpeech;
import wordwizard.service.validation.pos.PosValidator;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorsTest {

    private final TextValidator textValidator = new TextValidator();
    private final PosValidator posValidator = new PosValidator();

    @Test
    void validWordsPass() {
        assertDoesNotThrow(() -> textValidator.validateWords(List.of("cat", "well-known")));
    }

    @Test
    void invalidWordsThrow() {
        assertThrows(InvalidWordException.class,
                () -> textValidator.validateWords(List.of("two words")));
        assertThrows(InvalidWordException.class,
                () -> textValidator.validateWords(List.of("number7")));
        assertThrows(InvalidWordException.class,
                () -> textValidator.validateWords(Arrays.asList((String) null)));
        assertThrows(InvalidWordException.class,
                () -> textValidator.validateWords(List.of("  ")));
    }

    @Test
    void definitionsValidateContentAndBlankness() {
        assertDoesNotThrow(() -> textValidator.validateDefinitions(
                List.of("a small, furry animal (pet)!")));
        assertThrows(InvalidDefinitionException.class,
                () -> textValidator.validateDefinitions(List.of("   ")));
        assertThrows(InvalidDefinitionException.class,
                () -> textValidator.validateDefinitions(Arrays.asList((String) null)));
    }

    @Test
    void themeAndStartsWithAllowNullButValidateContent() {
        assertDoesNotThrow(() -> textValidator.validateTheme(null));
        assertDoesNotThrow(() -> textValidator.validateTheme("animals"));
        assertThrows(InvalidThemeException.class, () -> textValidator.validateTheme("an imals!"));

        assertDoesNotThrow(() -> textValidator.validateStart(null));
        assertDoesNotThrow(() -> textValidator.validateStart("ca"));
        assertThrows(InvalidStartsWithException.class, () -> textValidator.validateStart("c4"));
    }

    @Test
    void posValidatorAcceptsKnownPosAndNull() {
        assertDoesNotThrow(() -> posValidator.validatePos(null));
        assertDoesNotThrow(() -> posValidator.validatePos("noun"));
        assertDoesNotThrow(() -> posValidator.validatePos("NOUN"));

        InvalidWordException e = assertThrows(InvalidWordException.class,
                () -> posValidator.validatePos("nounish"));
        assertTrue(e.getMessage().contains("nounish"));
        assertTrue(e.getMessage().contains("noun"));
    }

    @Test
    void partOfSpeechFromStringIsCaseInsensitiveAndNullSafe() {
        assertEquals(PartOfSpeech.VERB, PartOfSpeech.fromString("Verb").orElseThrow());
        assertTrue(PartOfSpeech.fromString(null).isEmpty());
        assertTrue(PartOfSpeech.fromString("unknown").isEmpty());
    }
}
