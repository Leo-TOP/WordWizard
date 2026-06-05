package wordwizard.businesslogic.themesprocessing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wordwizard.businesslogic.ai.promptbuilding.PromptBuilder;
import wordwizard.businesslogic.ai.responseprocessing.AIResponseProcessor;
import wordwizard.businesslogic.ai.responseprocessing.dto.BatchThemeResponse;
import wordwizard.businesslogic.ai.responseprocessing.dto.SingleThemeResponse;
import wordwizard.businesslogic.dtomapping.aimapping.AIResponseDtoMapper;
import wordwizard.businesslogic.dtomapping.aimapping.AssignmentInput;
import wordwizard.repository.aiclients.GeminiChatClient;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Theme;
import wordwizard.repository.entities.Word;

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

        List<Theme> allThemes = fetchExistingThemes();
        String prompt = buildBatchPrompt(words, allThemes);
        BatchThemeResponse batchResponse = callAIForBatch(prompt);
        List<AssignmentInput> inputs = mapToAssignments(batchResponse, words);
        saveAssignments(inputs);

        log.info("Applied {} theme assignments for {} words", inputs.size(), words.size());
    }

    @Transactional
    public String assignThemeToDefinition(Word word, Definition def) {
        List<Theme> existingThemes = fetchExistingThemes();
        String prompt = buildSinglePrompt(word, def, existingThemes);
        SingleThemeResponse singleResponse = callAIForSingle(prompt);
        AssignmentInput input = mapToAssignment(singleResponse, word, def);
        saveAssignment(input);

        return input.themeName();
    }


    private List<Theme> fetchExistingThemes() {
        return repository.findAllThemes();
    }

    private String buildBatchPrompt(List<Word> words, List<Theme> allThemes) {
        return promptBuilder.buildThemeAssignmentPrompt(words, allThemes);
    }

    private String buildSinglePrompt(Word word, Definition def, List<Theme> existingThemes) {
        return promptBuilder.buildSingleWordPrompt(word, def, existingThemes);
    }

    private BatchThemeResponse callAIForBatch(String prompt) {
        String response = aiClient.generateContent(prompt);
        return responseProcessor.parseBatchResponse(response);
    }

    private SingleThemeResponse callAIForSingle(String prompt) {
        String response = aiClient.generateContent(prompt);
        return responseProcessor.parseSingleResponse(response);
    }

    private List<AssignmentInput> mapToAssignments(BatchThemeResponse response, List<Word> words) {
        return mapper.toAssignments(response, words);
    }

    private AssignmentInput mapToAssignment(SingleThemeResponse response, Word word, Definition def) {
        return mapper.toAssignment(response, word, def);
    }

    private void saveAssignments(List<AssignmentInput> inputs) {
        for (AssignmentInput input : inputs) {
            saveAssignment(input);
        }
    }

    private void saveAssignment(AssignmentInput input) {
        if (input.definitionId() != null) {
            Long themeId = repository.getOrCreateTheme(input.themeName());
            repository.assignThemeToDefinition(input.wordId(), themeId, input.definitionId());
        }
    }
}