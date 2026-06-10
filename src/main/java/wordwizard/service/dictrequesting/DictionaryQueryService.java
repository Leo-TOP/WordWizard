package wordwizard.service.dictrequesting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.models.Theme;
import wordwizard.models.VocabularyStats;
import wordwizard.models.Word;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DictionaryQueryService {
    private final DatabaseManager repository;

    public Map<String, List<Word>> getFilteredWords(FilterRequest request) {
        // a null request means "no filters" (e.g. export without filter options)
        FilterRequest filter = request != null ? request : new FilterRequest(null, null, null);
        return repository.findFilteredWordsGroupedByTheme(filter.theme(),
                filter.partOfSpeech(),
                filter.startsWith());
    }

    public List<Theme> getAllThemes() {
        return repository.findAllThemes();
    }

    public VocabularyStats getStatistics() {
        return repository.getStatistics();
    }
}
