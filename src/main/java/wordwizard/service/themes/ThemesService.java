package wordwizard.service.themes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wordwizard.exceptions.GeminiApiException;
import wordwizard.exceptions.ThemeAssignmentException;
import wordwizard.service.ai.prompt.PromptBuilder;
import wordwizard.service.ai.response.AIResponseProcessor;
import wordwizard.service.ai.response.dto.BatchThemeResponse;
import wordwizard.service.ai.mapping.AIResponseDtoMapper;
import wordwizard.service.ai.mapping.AssignmentInput;
import wordwizard.infrastructure.GeminiChatClient;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.models.Theme;
import wordwizard.models.Word;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThemesService {
    private final GeminiChatClient aiClient;
    private final PromptBuilder promptBuilder;
    private final AIResponseProcessor responseProcessor;
    private final AIResponseDtoMapper mapper;
    private final DatabaseManager repository;

    @Transactional
    public void assignThemesToWords(List<Word> words) {
            if (words == null || words.isEmpty()) return;

            List<Theme> allThemes = repository.findAllThemes();
            String prompt = promptBuilder.buildThemeAssignmentPrompt(words, allThemes);

            BatchThemeResponse batchResponse;
            try {
                batchResponse = responseProcessor.parseBatchResponse(
                        aiClient.generateContent(prompt));
            } catch (GeminiApiException e) {
                throw new ThemeAssignmentException(
                        "Words were saved, but theme assignment failed: " + e.getMessage(), e);
            }

            List<AssignmentInput> inputs = mapper.toAssignments(batchResponse, words);
            inputs.forEach(this::saveAssignment);

            log.info("Applied {} theme assignments for {} words", inputs.size(), words.size());
    }

    private void saveAssignment(AssignmentInput input) {
        if (input.definitionId() != null) {
            Long themeId = repository.getOrCreateTheme(input.themeName());
            repository.assignThemeToDefinition(input.wordId(), themeId, input.definitionId());
        }
    }
}