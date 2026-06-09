package wordwizard.service.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wordwizard.service.dictrequesting.DictionaryQueryService;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.export.exporters.Exporter;
import wordwizard.service.export.exporters.ExporterFactory;
import wordwizard.models.Word;
import wordwizard.service.export.fileprocessing.ExportFileProcessor;
import wordwizard.util.FileUtil;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {
    private final DictionaryQueryService dictionaryQueryService;
    private final ExporterFactory exporterFactory;

    public void export(ExportRequest request) {
        Exporter exporter = exporterFactory.getExporter(request.format());

        Map<String, List<Word>> groupedWords = dictionaryQueryService.getWords(request.filterRequest());
        if (groupedWords.isEmpty()) {
            log.warn("No words found for the given filters — export skipped.");
            return;
        }

        byte[] content = exporter.export(groupedWords);

        var fileProcessor = new ExportFileProcessor(request.outputPath(), request.format());
        Path filePath = fileProcessor.process();
        FileUtil.writeBinaryToFile(filePath, content);

        log.info("Exported {} word(s) to {} (format: {})",
               groupedWords.values().
                       stream().
                       mapToLong(Collection::size).
                       sum(), request.outputPath(), request.format());
    }
}
