package wordwizard.service.validation.pos;

import wordwizard.exceptions.InvalidWordException;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class PosValidator {
    private PosValidator() {}

    public static void validatePos(String pos) {
        if (pos == null) return;

        if (PartOfSpeech.fromString(pos).isEmpty()) {
            throw new InvalidWordException(
                "Unknown part of speech: \"" + pos + "\". Supported: " + supportedLabels());
        }
    }

    private static String supportedLabels() {
        return Arrays.stream(PartOfSpeech.values())
                .map(PartOfSpeech::getLabel)
                .collect(Collectors.joining(", "));
    }
}
