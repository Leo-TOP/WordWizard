package wordwizard.service.dictrequesting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.models.Theme;
import wordwizard.models.Word;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryQueryService {
    private final DatabaseManager repository;

    public List<Word> getWords(FilterRequest request) {
        if (request == null || request.isEmpty()){
           return repository.getAllWords();
        }

        return repository.findWordsWithFiltering(request);
    }

    public List<Theme> getAllThemes() {
        return repository.findAllThemes();
    }
}
