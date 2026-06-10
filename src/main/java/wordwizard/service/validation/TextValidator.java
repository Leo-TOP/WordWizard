package wordwizard.service.validation;

import org.springframework.stereotype.Component;
import wordwizard.exceptions.InvalidDefinitionException;
import wordwizard.exceptions.InvalidStartsWithException;
import wordwizard.exceptions.InvalidThemeException;
import wordwizard.exceptions.InvalidWordException;

import java.util.List;

@Component
public class TextValidator {
    private static final String WORD_PATTERN       = "^[A-Za-z-]+$";
    private static final String DEFINITION_PATTERN = "^[\\w\\s\\p{Punct}]+$";

    public void validateWords(List<String> words) {
        words.forEach(w -> {
            if (w == null || w.isBlank())
                throw new InvalidWordException("Word must not be blank");
            if (!w.matches(WORD_PATTERN))
                throw new InvalidWordException("Word must contain only letters and hyphens. Got: \"" + w + "\"");
        });
    }

    public void validateDefinitions(List<String> definitions) {
        definitions.forEach(d -> {
            if (d == null || d.isBlank())
                throw new InvalidDefinitionException("Definition must not be blank");
            if (!d.matches(DEFINITION_PATTERN))
                throw new InvalidDefinitionException("Invalid definition. Got: \"" + d + "\"");
        });
    }

    public void validateTheme(String theme) {
        if (theme == null) return;
        if (!theme.matches(WORD_PATTERN))
            throw new InvalidThemeException("Theme must contain only letters and hyphens. Got: \"" + theme + "\"");
    }

    public void validateStart(String start) {
        if (start == null) return;
        if (!start.matches(WORD_PATTERN))
            throw new InvalidStartsWithException(
                    "startsWith must contain only letters and hyphens. Got: \"" + start + "\"");
    }
}
