package wordwizard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordwizard.models.SimilarWord;
import wordwizard.models.VocabularyStats;
import wordwizard.service.dictrequesting.DictionaryQueryService;
import wordwizard.service.export.ExportService;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.service.save.SaveService;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.save.message.SaveResult;
import wordwizard.service.similarword.SimilarityService;
import wordwizard.models.Theme;
import wordwizard.models.Word;
import wordwizard.service.similarword.dto.SimilarWordRequestForDefinition;
import wordwizard.service.similarword.dto.SimilarWordRequestForWord;
import wordwizard.service.validation.TextValidator;
import wordwizard.service.validation.pos.PosValidator;
import wordwizard.service.wordsfetching.WordsImportingService;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CentralService {
        private final ExportService exportService;
        private final DictionaryQueryService dictionaryQueryService;
        private final WordsImportingService wordsImportingService;
        private final SimilarityService similarityService;
        private final SaveService saveService;

        public Word getWord(WordRequest request) {
                TextValidator.validateWords(List.of(request.word()));
                PosValidator.validatePos(request.partOfSpeech());

                return wordsImportingService.fetchAndSaveWord(request);
        }

        public List<Word> getWords(List<String> words) {
                TextValidator.validateWords(words);

                return wordsImportingService.fetchAndSaveWords(words);
        }

        public Map<String, List<Word>> getWordsFromUserDictionary(FilterRequest request) {
                TextValidator.validateStart(request.startsWith());
                PosValidator.validatePos(request.partOfSpeech());

                return dictionaryQueryService.getWords(request);
        }

        public List<Theme> getAllThemes() {
                return dictionaryQueryService.getAllThemes();
        }

        public List<SimilarWord> findByDefinition(SimilarWordRequestForDefinition request) {
                TextValidator.validateDefinitions(List.of(request.definition()));
                return similarityService.findByDescription(request);
        }

        public List<SimilarWord> findSimilar(SimilarWordRequestForWord request) {
                TextValidator.validateWords(List.of(request.word()));
                PosValidator.validatePos(request.partOfSpeech());

                return similarityService.findSimilarToWord(request);
        }

        public List<SaveResult> addUserWords(List<UserWordRequest> requests) {
                TextValidator.validateWords(requests.stream().map(UserWordRequest::word).toList());
                TextValidator.validateDefinitions(requests.stream().map(UserWordRequest::definition).toList());
                requests.forEach(r -> PosValidator.validatePos(r.partOfSpeech()));

                return saveService.saveWords(requests);
        }

        public void exportWords(ExportRequest exportRequest) {
                FilterRequest f = exportRequest.filterRequest();
                if (f != null) {
                        TextValidator.validateStart(f.startsWith());
                        PosValidator.validatePos(f.partOfSpeech());
                }

                exportService.export(exportRequest);
        }

        public VocabularyStats getStatistics() {
                return dictionaryQueryService.getStatistics();
        }
}