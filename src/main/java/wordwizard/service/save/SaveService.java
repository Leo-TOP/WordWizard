package wordwizard.service.save;

import autovalue.shaded.org.checkerframework.checker.nullness.qual.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.service.embedding.EmbeddingService;
import wordwizard.service.save.dto.SaveDtoMapping;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.save.message.SaveResult;
import wordwizard.service.save.message.SaveStatus;
import wordwizard.service.similarword.SimilarityService;
import wordwizard.service.themes.ThemesService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveService {
    private final DatabaseManager repository;
    private final SaveDtoMapping dtoMapping;
    private final EmbeddingService embeddingService;
    private final ThemesService themesService;
    private final SimilarityService similarityService;
    private final ExecutorService executor;

    public List<SaveResult> saveWords(List<UserWordRequest> requests) {
        List<CompletableFuture<ProcessOutcome>> futures = requests.stream()
                .map(req -> CompletableFuture
                        .supplyAsync(() -> processWithContext(req), executor)
                        .orTimeout(15, TimeUnit.SECONDS))
                .toList();

        List<ProcessOutcome> outcomes = futures.stream()
                .map(f -> {
                    try {
                        return f.join();
                    } catch (Exception e) {
                        Throwable cause = e.getCause() != null ? e.getCause() : e;
                        log.warn("Failed to save word: {}", cause.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        List<Word> forThemes = outcomes.stream()
                .map(ProcessOutcome::tempWord)
                .filter(Objects::nonNull)
                .toList();

        if (!forThemes.isEmpty()) {
            themesService.assignThemesToWords(forThemes);
        }

        return outcomes.stream().map(ProcessOutcome::result).toList();
    }

    private ProcessOutcome processWithContext(UserWordRequest request) {
        SaveResult result = processWord(request);

        if (result.status() != SaveStatus.WORD_SAVED
                && result.status() != SaveStatus.DEFINITION_ADDED) {
            return new ProcessOutcome(result, null);
        }

        Word fullWord = repository.findByWord(request.word())
                .orElseThrow(() -> new IllegalStateException(
                        "Word not found after save: " + request.word()));
        Word tempWord = buildTempWordWithOnlyNewDef(fullWord, request);
        return new ProcessOutcome(result, tempWord);
    }


    private SaveResult processWord(UserWordRequest request) {
        Optional<Word> existingWordOpt = repository.findByWord(request.word());
        return existingWordOpt
                .map(word -> addDefinitionToExistingWord(word, request))
                .orElseGet(() -> saveNewWord(request));
    }

    private SaveResult saveNewWord(UserWordRequest request) {
        try {
            Word newWord = dtoMapping.mapToWord(request);
            Word savedWord = repository.saveWord(newWord);
            embeddingService.generateAndSaveEmbeddingsForWord(savedWord);
            return new SaveResult(request.word(), SaveStatus.WORD_SAVED, "New word saved.");
        } catch (DataIntegrityViolationException e) {
            log.debug("Race condition on word '{}', retrying as definition add", request.word());
            return repository.findByWord(request.word())
                    .map(existing -> addDefinitionToExistingWord(existing, request))
                    .orElseThrow(() -> new IllegalStateException(
                            "Word '" + request.word() + "' missing after constraint violation"));
        }
    }

    private Word buildTempWordWithOnlyNewDef(Word fullWord, UserWordRequest request) {
        Definition newDef = fullWord.definitions().stream()
                .filter(d -> d.text().equals(request.definition()) && "user".equals(d.source()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("New definition not found after save"));
        return new Word(fullWord.id(), fullWord.word(),
                fullWord.createdAt(), fullWord.updatedAt(), List.of(newDef));
    }

    private SaveResult addDefinitionToExistingWord(Word existingWord, UserWordRequest request) {
        float[] newEmbedding = embeddingService.getEmbedding(request.definition());

        boolean isDuplicate = similarityService.isDuplicateDefinition(newEmbedding, existingWord.id());

        if (isDuplicate) {
            return new SaveResult(request.word(), SaveStatus.DUPLICATE_SKIPPED,
                    "Definition too similar to an existing one.");
        }

        Definition newDef = dtoMapping.mapToDefinition(request.definition(), request.partOfSpeech());
        Long defId  = repository.addDefinitionToWord(existingWord.id(), newDef);
        repository.updateDefinitionEmbedding(defId, newEmbedding);

        return new SaveResult(request.word(), SaveStatus.DEFINITION_ADDED, "New definition added.");
    }

    private record ProcessOutcome(SaveResult result, @Nullable Word tempWord) {}
}
