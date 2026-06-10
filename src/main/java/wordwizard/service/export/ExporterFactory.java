package wordwizard.service.export;

import org.springframework.stereotype.Component;
import wordwizard.exceptions.InvalidFormatException;
import wordwizard.service.export.exporters.Exporter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExporterFactory {
    private final Map<String, Exporter> exporters;

    public ExporterFactory(List<Exporter> exporterList) {
        exporters = exporterList.stream()
                .collect(Collectors.toUnmodifiableMap(Exporter::getFormat, e -> e));
    }

    public Exporter getExporter(String format) {
        if (format == null || format.isBlank()) {
            throw new InvalidFormatException(
                    "Export format must not be empty. Available: " + exporters.keySet());
        }
        Exporter exporter = exporters.get(format.toLowerCase());
        if (exporter == null) {
            throw new InvalidFormatException(
                    "Unsupported export format: \"" + format + "\". Available: " + exporters.keySet());
        }
        return exporter;
    }
}
