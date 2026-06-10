package wordwizard.service.validation.pos;

import org.springframework.stereotype.Component;
import wordwizard.exceptions.InvalidWordException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class PosValidator {
    public void validatePos(String pos) {
        if (pos == null) return;

        if (PartOfSpeech.fromString(pos).isEmpty()) {
            throw new InvalidWordException(
                "Unknown part of speech: \"" + pos + "\". Supported: " + supportedLabels());
        }
    }

    private String supportedLabels() {
        return Arrays.stream(PartOfSpeech.values())
                .map(PartOfSpeech::getLabel)
                .collect(Collectors.joining(", "));
    }
}
