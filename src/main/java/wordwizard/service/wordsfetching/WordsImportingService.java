package wordwizard.service.wordsfetching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import wordwizard.exceptions.BatchImportException;
import wordwizard.exceptions.WordNotFoundException;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.externaldictionaries.dto.ApiDtoMapper;
import wordwizard.repository.externaldictionaries.DictionaryApiClient;
import wordwizard.repository.externaldictionaries.dto.DictionaryApiResponse;
import wordwizard.service.embedding.EmbeddingService;
import wordwizard.service.themes.ThemesService;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class WordsImportingService {
    private static final int IMPORT_TIMEOUT_SECONDS = 30;

    private final DictionaryApiClient apiClient;
    private final DatabaseManager dbManager;
    private final ApiDtoMapper apiMapper;
    private final ThemesService themesService;
    private final EmbeddingService embeddingService;

    @Qualifier("wordImportExecutor")
    private final ExecutorService pool;

    public WordsImportingService(DictionaryApiClient apiClient,
                                 DatabaseManager dbManager,
                                 ApiDtoMapper apiMapper,
                                 ThemesService themesService,
                                 EmbeddingService embeddingService, ExecutorService pool) {
        this.apiClient = apiClient;
        this.dbManager = dbManager;
        this.apiMapper = apiMapper;
        this.themesService = themesService;
        this.embeddingService = embeddingService;
        this.pool = pool;
    }

    public Word fetchAndSaveWord(WordRequest wordRequest) {
        Word savedWord = processWord(wordRequest.word());
        themesService.assignThemesToWords(List.of(savedWord));
        return applyFilters(savedWord, wordRequest.partOfSpeech(), wordRequest.limit());
    }

    public List<Word> fetchAndSaveWords(List<String> words) {
        List<String> unique = words.stream().distinct().toList();

        List<CompletableFuture<Word>> futures = unique.stream()
                .map(word -> CompletableFuture
                        .supplyAsync(() -> processWord(word), pool)
                        .orTimeout(IMPORT_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                .toList();

        List<Word> processedWords = new ArrayList<>();
        Map<String, String> failures = new LinkedHashMap<>();

        for (int i = 0; i < futures.size(); i++) {
            String word = unique.get(i);
            try {
                processedWords.add(futures.get(i).join());
            } catch (Exception e) {
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                log.warn("Failed to import word '{}': {}", word, cause.getMessage());
                failures.put(word, describeFailure(cause));
            }
        }

        themesService.assignThemesToWords(processedWords);

        if (!failures.isEmpty()) {
            throw new BatchImportException(
                    processedWords.stream().map(Word::word).toList(), failures);
        }
        return processedWords;
    }

    private String describeFailure(Throwable cause) {
        if (cause instanceof TimeoutException) {
            return "import timed out after " + IMPORT_TIMEOUT_SECONDS + " seconds";
        }
        return cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
    }

    private Word processWord(String word) {
        Optional<Word> existing = dbManager.findByWord(word);
        if (existing.isPresent()) return existing.get();

        DictionaryApiResponse response = apiClient.fetchWord(word);
        if (response == null) {
            throw new WordNotFoundException("Word \"" + word + "\" was not found in the dictionary");
        }

        Word newWord = apiMapper.mapToWord(response);

        try {
            Word savedWord = dbManager.saveWord(newWord);
            embeddingService.generateAndSaveEmbeddingsForWord(savedWord);
            return savedWord;
        } catch (DataIntegrityViolationException e) {
            log.debug("Word \"{}\" was saved concurrently, loading from DB", word);
            return dbManager.findByWord(word)
                    .orElseThrow(() -> new WordNotFoundException(
                            "Word \"" + word + "\" disappeared after a concurrent save"));
        }
    }

    private Word applyFilters(Word word, String partOfSpeech, Integer limit) {
        List<Definition> defs = word.definitions();

        if (partOfSpeech != null) {
            defs = defs.stream()
                    .filter(d -> partOfSpeech.equals(d.partOfSpeech()))
                    .toList();
        }

        if (limit != null && limit > 0 && limit < defs.size()) {
            defs = defs.subList(0, limit);
        }

        if (defs.isEmpty() && partOfSpeech != null) {
            throw new WordNotFoundException(
                    "\"" + word.word() + "\" has no definitions for part of speech \"" + partOfSpeech + "\"");
        }

        return new Word(word.id(), word.word(), word.createdAt(), word.updatedAt(), defs);
    }
}
