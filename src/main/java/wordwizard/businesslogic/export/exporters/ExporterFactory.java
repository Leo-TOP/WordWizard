package wordwizard.businesslogic.export.exporters;

import org.springframework.stereotype.Component;

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
        Exporter exporter = exporters.get(format.toLowerCase());
        if (exporter == null) {
            throw new IllegalArgumentException(
                    "Unsupported export format: \"" + format + "\". Available: " + exporters.keySet());
        }
        return exporter;
    }
}
