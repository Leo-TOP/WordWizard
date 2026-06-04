package wordwizard.businesslogic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wordwizard.businesslogic.embeddingapplication.EmbeddingService;
import wordwizard.businesslogic.export.ExportService;
import wordwizard.businesslogic.themesprocessing.ThemesService;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.entities.Theme;
import wordwizard.repository.entities.Word;
import wordwizard.repository.externalsourcefetching.DictionaryApiClient;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordWizardService {
        private final DictionaryApiClient apiClient;
        private final DatabaseManager dbManager;
        private final ThemesService themesService;
        private final EmbeddingService embeddingService;
        private final ExportService exportService;

        public Word getWord(String word, Integer limit) { ... }

        public List<Word> getWords(List<String> words) { ... }

        public List<Word> filterWords(String theme, String pos, String search) { ... }

        public List<Theme> getAllThemes() { ... }

        public List<Word> findByDefinition(String definition) { ... }

        public List<Word> findSimilar(String word) { ... }

        public void addWord(String word, String definition, String pos, String theme) { ... }

        public void exportWords(String theme, String format, String outputPath) { ... }
    }
}