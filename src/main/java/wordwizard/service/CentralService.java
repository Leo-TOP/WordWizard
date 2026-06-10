package wordwizard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordwizard.exceptions.InvalidRequestException;
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
        private final TextValidator textValidator;
        private final PosValidator posValidator;

        public Word getWord(WordRequest request) {
                requireRequest(request, "word request");
                textValidator.validateWords(List.of(request.word()));
                posValidator.validatePos(request.partOfSpeech());

                return wordsImportingService.fetchAndSaveWord(request);
        }

        public List<Word> getWords(List<String> words) {
                requireNonEmpty(words, "word");
                textValidator.validateWords(words);

                return wordsImportingService.fetchAndSaveWords(words);
        }

        public Map<String, List<Word>> filter(FilterRequest request) {
                requireRequest(request, "filter request");
                textValidator.validateStart(request.startsWith());
                textValidator.validateTheme(request.theme());
                posValidator.validatePos(request.partOfSpeech());

                return dictionaryQueryService.getFilteredWords(request);
        }

        public List<Theme> getAllThemes() {
                return dictionaryQueryService.getAllThemes();
        }

        public List<SimilarWord> findByDefinition(SimilarWordRequestForDefinition request) {
                requireRequest(request, "definition request");
                textValidator.validateDefinitions(List.of(request.definition()));
                return similarityService.findByDescription(request);
        }

        public List<SimilarWord> findSimilar(SimilarWordRequestForWord request) {
                requireRequest(request, "similarity request");
                textValidator.validateWords(List.of(request.word()));
                posValidator.validatePos(request.partOfSpeech());

                return similarityService.findSimilarToWord(request);
        }

        public List<SaveResult> addUserWords(List<UserWordRequest> requests) {
                requireNonEmpty(requests, "word–definition pair");
                textValidator.validateWords(requests.stream().map(UserWordRequest::word).toList());
                textValidator.validateDefinitions(requests.stream().map(UserWordRequest::definition).toList());

                return saveService.saveWords(requests);
        }

        public void exportWords(ExportRequest exportRequest) {
                requireRequest(exportRequest, "export request");
                FilterRequest f = exportRequest.filterRequest();
                if (f != null) {
                        textValidator.validateStart(f.startsWith());
                        textValidator.validateTheme(f.theme());
                        posValidator.validatePos(f.partOfSpeech());
                }

                exportService.export(exportRequest);
        }

        public VocabularyStats getStatistics() {
                return dictionaryQueryService.getStatistics();
        }

        private static void requireRequest(Object request, String name) {
                if (request == null) {
                        throw new InvalidRequestException("The " + name + " must not be null");
                }
        }

        private static void requireNonEmpty(List<?> items, String itemName) {
                if (items == null || items.isEmpty()) {
                        throw new InvalidRequestException("At least one " + itemName + " is required");
                }
        }
}