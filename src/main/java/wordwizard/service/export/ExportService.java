package wordwizard.service.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wordwizard.exceptions.ExportException;
import wordwizard.service.dictrequesting.DictionaryQueryService;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.export.exporters.Exporter;
import wordwizard.models.Word;
import wordwizard.service.export.fileprocessing.ExportFileProcessor;
import wordwizard.util.FileUtil;

import java.nio.file.Path;
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

        var fileProcessor = new ExportFileProcessor(request.outputPath(), request.format());
        Path filePath = fileProcessor.process();

        Map<String, List<Word>> groupedWords = dictionaryQueryService.getFilteredWords(request.filterRequest());

        if (groupedWords.isEmpty()) {
            throw new ExportException("No words match the given filters — nothing to export");
        }

        byte[] content = exporter.export(groupedWords);
        FileUtil.writeBinaryToFile(filePath, content);

        log.info("Exported {} word(s) to {} (format: {})",
         groupedWords.values().stream()
                .flatMap(List::stream)
                .map(Word::id)
                .distinct()
                .count(), request.outputPath(), request.format());
    }
}
