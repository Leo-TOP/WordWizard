package wordwizard.service.ai.prompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.models.Theme;
import wordwizard.models.Word;
import wordwizard.util.FileUtil;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromptBuilder {
            private final PromptObjectFormatter formatter;
            private static final String BATCH_PROMPT_PATH = "prompts/theme-assignment-batch.txt";

            public String buildThemeAssignmentPrompt(List<Word> words, List<Theme> allThemes) {
                String template = FileUtil.readResource(BATCH_PROMPT_PATH);
                String existingThemes = formatter.formatExistingThemes(allThemes);
                String wordDefinitions = formatter.formatWordsForPrompt(words);

                return String.format(template, existingThemes, wordDefinitions);
            }
}