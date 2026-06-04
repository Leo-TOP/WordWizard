package wordwizard.businesslogic.themesprocessing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wordwizard.businesslogic.ai.promptbuilding.PromptBuilder;
import wordwizard.businesslogic.ai.responseprocessing.AIResponseProcessor;
import wordwizard.businesslogic.ai.responseprocessing.dto.AIResponseDtoMapper;
import wordwizard.repository.aiclients.GeminiChatClient;
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

    @Transactional
    public void assignThemesToWords(List<Word> words) {
        if (words == null || words.isEmpty()) return;

        String prompt = promptBuilder.buildThemeAssignmentPrompt(words);
        String response = aiClient.generateContent(prompt);

        var batchResponse = responseProcessor.parseBatchResponse(response);
        if (batchResponse.themeAssignments().isEmpty()) {
            log.warn("AI returned no theme assignments");
            return;
        }

        mapper.applyBatchResponse(batchResponse, words);
        log.info("Assigned themes for {} words", words.size());
    }

    @Transactional
    public String assignThemeToSingleWord(Word word) {
        String defText = word.definitions().isEmpty() ? "" : word.definitions().get(0).text();

        String prompt = promptBuilder.buildSingleWordPrompt(word.word(), defText);
        String response = aiClient.generateContent(prompt);

        var singleResponse = responseProcessor.parseSingleResponse(response);
        return mapper.applySingleResponse(singleResponse, word);
    }
}
