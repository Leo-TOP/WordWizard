package wordwizard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordwizard.models.SimilarWord;
import wordwizard.models.VocabularyStats;
import wordwizard.service.dictrequesting.DictionaryQueryService;
import wordwizard.service.export.ExportService;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.service.save.UserWordRequest;
import wordwizard.service.similarword.SimilarWordService;
import wordwizard.models.Theme;
import wordwizard.models.Word;
import wordwizard.service.similarword.dto.SimilarWordRequestForDefinition;
import wordwizard.service.similarword.dto.SimilarWordRequestForWord;
import wordwizard.service.validation.pos.PosValidator;
import wordwizard.service.validation.WordsValidator;
import wordwizard.service.wordsfetching.WordsImportingService;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CentralService {
        private final ExportService exportService;
        private final DictionaryQueryService dictionaryQueryService;
        private final WordsImportingService wordsImportingService;
        private final SimilarWordService similarWordService;

        public Word getWord(WordRequest request) {
                WordsValidator.validateWord(request.word());
                PosValidator.validatePos(request.partOfSpeech());

                return wordsImportingService.fetchAndSaveWord(request);
        }

        public List<Word> getWords(List<String> words) {
                WordsValidator.validateWords(words);

                return wordsImportingService.fetchAndSaveWords(words);
        }

        public List<Word> getWordsFromUserDictionary(FilterRequest request) {
                return dictionaryQueryService.getWords(request);
        }

        public List<Theme> getAllThemes() {
                return dictionaryQueryService.getAllThemes();
        }

        public List<String> findByDefinition(SimilarWordRequestForDefinition request) {
                WordsValidator.validateWord(request.definition());
                return similarWordService.findByDescription(request);
        }

        public List<SimilarWord> findSimilar(SimilarWordRequestForWord request) {
                WordsValidator.validateWord(request.word());
                PosValidator.validatePos(request.partOfSpeech());

                return similarWordService.findSimilarToWord(request);
        }

        public void addUserWords(List<UserWordRequest> request) {}

        public void exportWords(ExportRequest exportRequest) {
                exportService.export(exportRequest);
        }

        public VocabularyStats getStatistics() {return null;}
}