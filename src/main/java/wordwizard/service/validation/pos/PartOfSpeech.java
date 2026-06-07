package wordwizard.service.validation.pos;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public enum PartOfSpeech {
    NOUN("noun"), VERB("verb"), ADJECTIVE("adjective"),
    ADVERB("adverb"), PREPOSITION("preposition"),
    CONJUNCTION("conjunction"), PRONOUN("pronoun"),
    INTERJECTION("interjection"), EXCLAMATION("exclamation");

    private final String label;

    public static Optional<PartOfSpeech> fromString(String value) {
        if (value == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(p -> p.label.equalsIgnoreCase(value))
                .findFirst();
    }
}
