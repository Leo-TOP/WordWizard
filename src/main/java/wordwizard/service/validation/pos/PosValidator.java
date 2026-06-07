package wordwizard.service.validation.pos;

import wordwizard.exceptions.InvalidWordRequestException;

import java.util.Set;

public final class PosValidator {
    private static final Set<String> SUPPORTED_POS = Set.of(
            "noun", "verb", "adjective", "adverb",
            "preposition", "conjunction", "pronoun", "interjection", "exclamation"
    );

    public static void validatePos(String pos) {
        if (pos != null && !SUPPORTED_POS.contains(pos)) {
            throw new InvalidWordRequestException("Unknown part of speech: " + pos);
        }
    }
}
