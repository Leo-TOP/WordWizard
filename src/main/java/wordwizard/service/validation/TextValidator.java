package wordwizard.service.validation;

import wordwizard.exceptions.InvalidDefinitionException;
import wordwizard.exceptions.InvalidStartsWithException;
import wordwizard.exceptions.InvalidWordException;

import java.util.List;


public final class TextValidator {
    private static final String WORD_PATTERN       = "^[A-Za-z\\s-]+$";
    private static final String DEFINITION_PATTERN = "^[\\w\\s\\p{Punct}]+$";

    private TextValidator() {}

    public static void validateWords(List<String> words) {
        words.forEach(w -> {
            if (w == null || w.isBlank())
                throw new InvalidWordException("Word must not be blank");
            if (!w.matches(WORD_PATTERN))
                throw new InvalidWordException("Invalid word: \"" + w + "\"");
        });
    }

    public static void validateDefinitions(List<String> definitions) {
        definitions.forEach(d -> {
            if (d == null || d.isBlank())
                throw new InvalidDefinitionException("Definition must not be blank");
            if (!d.matches(DEFINITION_PATTERN))
                throw new InvalidDefinitionException("Invalid definition: \"" + d + "\"");
        });
    }

    public static void validateStart(String start) {
        if (start != null && !start.matches(WORD_PATTERN))
            throw new InvalidStartsWithException(
                    "startsWith must contain only letters, spaces, or hyphens. Got: \"" + start + "\"");
    }
}
