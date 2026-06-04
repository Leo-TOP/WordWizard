package wordwizard.businesslogic.ai.responseprocessing.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Word;

import java.util.List;

/**
 * Takes AI response DTOs and saves theme assignments directly to the database.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AIResponseDtoMapper {

    private final DatabaseManager repository;

    /**
     * Process batch response — save all assignments.
     */
    public void applyBatchResponse(BatchThemeResponse response, List<Word> words) {
        for (ThemeAssignment assignment : response.themeAssignments()) {
            applyAssignment(assignment, words);
        }
    }

    /**
     * Process single response — save one assignment.
     */
    public String applySingleResponse(SingleThemeResponse response, Word word) {
        String themeName = response.theme();
        if (themeName == null || themeName.isBlank()) {
            themeName = "unclassified";
        }
        themeName = themeName.toLowerCase().trim();

        Long defId = word.definitions().isEmpty() ? null : word.definitions().get(0).id();
        if (defId != null) {
            Long themeId = repository.getOrCreateTheme(themeName);
            repository.assignThemeToDefinition(word.id(), themeId, defId);
        }

        return themeName;
    }

    // ============================================================
    // PRIVATE
    // ============================================================

    private void applyAssignment(ThemeAssignment assignment, List<Word> words) {
        // Validate theme name
        String themeName = assignment.theme();
        if (themeName == null || themeName.isBlank()) {
            log.warn("Skipping assignment with empty theme for word '{}'", assignment.word());
            return;
        }
        themeName = themeName.toLowerCase().trim();

        // Find the word
        Word word = findWord(words, assignment.word());
        if (word == null) {
            log.warn("Word '{}' not found in batch", assignment.word());
            return;
        }

        // Find the definition by index
        int index = assignment.definitionIndex();
        List<Definition> defs = word.definitions();
        if (index < 0 || index >= defs.size()) {
            log.warn("Invalid definitionIndex {} for word '{}' (has {} definitions)",
                    index, assignment.word(), defs.size());
            return;
        }

        // Save
        Long themeId = repository.getOrCreateTheme(themeName);
        Long defId = defs.get(index).id();
        repository.assignThemeToDefinition(word.id(), themeId, defId);

        log.debug("Assigned '{}' def #{} to theme '{}'", assignment.word(), index, themeName);
    }

    private Word findWord(List<Word> words, String wordText) {
        return words.stream()
                .filter(w -> w.word().equalsIgnoreCase(wordText))
                .findFirst()
                .orElse(null);
    }
}