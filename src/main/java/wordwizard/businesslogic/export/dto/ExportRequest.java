package wordwizard.businesslogic.export.dto;

import java.nio.file.Path;

public record ExportRequest(
        String theme,
        String partOfSpeech,
        String startsWith,
        String format,
        Path outputPath
) {}