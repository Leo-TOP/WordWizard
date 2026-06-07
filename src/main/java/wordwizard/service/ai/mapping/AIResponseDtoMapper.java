package wordwizard.service.ai.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.service.ai.response.dto.BatchThemeResponse;
import wordwizard.service.ai.response.dto.SingleThemeResponse;
import wordwizard.service.ai.response.dto.ThemeAssignment;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AIResponseDtoMapper {

    public List<AssignmentInput> toAssignments(BatchThemeResponse response, List<Word> words) {
        if (response == null || response.themeAssignments() == null || response.themeAssignments().isEmpty()) {
            log.warn("AI returned no theme assignments");
            return List.of();
        }

        return response.themeAssignments().stream()
                .map(assignment -> toOptionalAssignment(assignment, words))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public AssignmentInput toAssignment(SingleThemeResponse response, Word word, Definition def) {
        String themeName = cleanThemeName(response.theme());
        return new AssignmentInput(word.id(), def.id(), themeName);
    }

    private Optional<AssignmentInput> toOptionalAssignment(ThemeAssignment assignment, List<Word> words) {
        if (!isValidAssignment(assignment)) {
            return Optional.empty();
        }

        String themeName = cleanThemeName(assignment.theme());
        Word word = findWord(words, assignment.word());
        if (word == null) {
            log.warn("Word '{}' not found in input batch", assignment.word());
            return Optional.empty();
        }

        if (!isValidDefIndex(assignment.definitionIndex(), word)) {
            log.warn("Invalid definitionIndex {} for '{}' (has {} defs)",
                    assignment.definitionIndex(), assignment.word(), word.definitions().size());
            return Optional.empty();
        }

        Definition def = word.definitions().get(assignment.definitionIndex());
        return Optional.of(new AssignmentInput(word.id(), def.id(), themeName));
    }


    private boolean isValidAssignment(ThemeAssignment assignment) {
        if (assignment == null) {
            return false;
        }

        if (assignment.word() == null || assignment.word().isBlank()) {
            log.warn("Skipping assignment with empty word");
            return false;
        }

        if (assignment.theme() == null || assignment.theme().isBlank()) {
            log.warn("Skipping empty theme for word '{}'", assignment.word());
            return false;
        }

        return true;
    }

    private boolean isValidDefIndex(int index, Word word) {
        return index >= 0 && index < word.definitions().size();
    }


    private String cleanThemeName(String themeName) {
        return themeName.toLowerCase().trim();
    }

    private Word findWord(List<Word> words, String wordText) {
        return words.stream()
                .filter(w -> w.word().equalsIgnoreCase(wordText))
                .findFirst()
                .orElse(null);
    }
}