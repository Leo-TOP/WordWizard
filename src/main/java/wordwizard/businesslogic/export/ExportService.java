package wordwizard.businesslogic.export;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wordwizard.businesslogic.export.dto.ExportRequest;
import wordwizard.businesslogic.export.exporters.ExporterFactory;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.entities.Word;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private final DatabaseManager repository;
    private final ExporterFactory exporterFactory;

    public void export(ExportRequest request) {
        List<Word> words = repository.findWordsForExport(
                request.theme(),
                request.partOfSpeech(),
                request.startsWith()
        );

        if (words.isEmpty()) {
            log.warn("No words found for the given filters — export skipped.");
            return;
        }

        byte[] content = exporterFactory
                .getExporter(request.format())
                .export(words);

        wordwizard.util.FileUtil.writeBinaryToFile(request.outputPath(), content);

        log.info("Exported {} word(s) to {} (format: {})",
                words.size(), request.outputPath(), request.format());
    }
}
