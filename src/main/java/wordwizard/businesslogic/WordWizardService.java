package wordwizard.businesslogic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.entities.Word;
import wordwizard.repository.externalsourcefetching.DictionaryApiClient;
import wordwizard.repository.externalsourcefetching.dto.DictionaryApiResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordWizardService {

    private final DictionaryApiClient apiClient;
    private final DatabaseManager dbManager;


    @Transactional
    public List<Word> lookupWords(List<String> words) {
        List<Word> result = new ArrayList<>();

        for (String word : words) {
            Word existing = dbManager.findByWord(word).orElse(null);
            if (existing != null) {
                log.info("'{}' already in database", word);
                result.add(existing);
                continue;
            }

            List<DictionaryApiResponse> responses = apiClient.fetchWord(word);
            if (responses.isEmpty()) {
                log.warn("No results for '{}'", word);
                continue;
            }

            Word newWord = mapToEntity(word, responses.getFirst());

            dbManager.save(newWord);
            log.info("Saved '{}' with {} definitions", word, newWord.getDefinitions().size());

            result.add(newWord);
        }

        return result;
    }
}