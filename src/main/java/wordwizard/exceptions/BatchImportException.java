package wordwizard.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class BatchImportException extends RuntimeException {
    /** Words that were imported and saved successfully before the failure was detected. */
    private final List<String> importedWords;
    /** Failed word -> human-readable reason. */
    private final Map<String, String> failures;

    public BatchImportException(List<String> importedWords, Map<String, String> failures) {
        super(buildMessage(importedWords, failures));
        this.importedWords = List.copyOf(importedWords);
        this.failures = Map.copyOf(failures);
    }

    private static String buildMessage(List<String> imported, Map<String, String> failures) {
        int total = imported.size() + failures.size();
        StringBuilder sb = new StringBuilder(
                "Imported " + imported.size() + " of " + total + " word(s).");
        if (!imported.isEmpty()) {
            sb.append(" Saved: ").append(String.join(", ", imported)).append(".");
        }
        sb.append(" Failed:");
        failures.forEach((word, reason) ->
                sb.append("\n  - ").append(word).append(": ").append(reason));
        return sb.toString();
    }
}
