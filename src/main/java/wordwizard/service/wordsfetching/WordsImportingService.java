package wordwizard.service.wordsfetching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WordsImportingService {

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
                        .orTimeout(30, TimeUnit.SECONDS))
                .toList();

        List<Word> processedWords = futures.stream()
                .map(f -> {
                    try {
                        return f.join();
                    } catch (Exception e) {
                        Throwable cause = e.getCause() != null ? e.getCause() : e;
                        log.warn("Failed to import word: {}", cause.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        themesService.assignThemesToWords(processedWords);
        return processedWords;
    }

    private Word processWord(String word) {
        Optional<Word> existing = dbManager.findByWord(word);
        if (existing.isPresent()) return existing.get();

        DictionaryApiResponse response = apiClient.fetchWord(word);
        if (response == null) throw new WordNotFoundException(word);

        Word newWord = apiMapper.mapToWord(response);

        try {
            Word savedWord = dbManager.saveWord(newWord);
            embeddingService.generateAndSaveEmbeddingsForWord(savedWord);
            return savedWord;
        } catch (DataIntegrityViolationException e) {
            log.debug("Word \"{}\" was saved concurrently, loading from DB", word);
            return dbManager.findByWord(word)
                    .orElseThrow(() -> new WordNotFoundException(word));
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
                    word.word() + " has no definitions for part of speech: " + partOfSpeech);
        }

        return new Word(word.id(), word.word(), word.createdAt(), word.updatedAt(), defs);
    }
}
