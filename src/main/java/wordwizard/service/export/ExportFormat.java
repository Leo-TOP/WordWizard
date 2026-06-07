package wordwizard.service.export;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wordwizard.exceptions.InvalidFormatException;

import java.util.Arrays;
import java.util.Set;

@RequiredArgsConstructor
public enum ExportFormat {
    DOCX("docx",     Set.of(".docx")),
    EXCEL("excel",   Set.of(".xlsx", ".xls", ".xlsm")),
    TEXT("text",     Set.of(".txt")),
    MARKDOWN("markdown", Set.of(".md"));

    private final String key;

    @Getter
    private final Set<String> validExtensions;

    public boolean supportsExtension(String ext) {
        return validExtensions.contains(ext);
    }

    public static ExportFormat fromString(String value) {
        return Arrays.stream(values())
                .filter(f -> f.key.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidFormatException(
                        "Unknown format: \"" + value + "\". Supported: " +
                                Arrays.stream(values()).map(f -> f.key).toList()));
    }
}