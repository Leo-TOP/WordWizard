package wordwizard.businesslogic.ai.promptbuilding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.entities.Word;
import wordwizard.util.ResourceFileUtil;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromptBuilder {

            private final DatabaseManager repository;
            private final PromptObjectFormatter formatter;

            private static final String BATCH_PROMPT_PATH = "prompts/theme-assignment-batch.txt";
            private static final String SINGLE_PROMPT_PATH = "prompts/theme-assignment-single.txt";


            public String buildThemeAssignmentPrompt(List<Word> words) {
                String template = ResourceFileUtil.readResource(BATCH_PROMPT_PATH);
                String existingThemes = formatter.formatExistingThemes(repository.findAllThemes());
                String wordDefinitions = formatter.formatWordsForPrompt(words);

                return String.format(template, existingThemes, wordDefinitions);
            }


            public String buildSingleWordPrompt(String word, String definition) {
                String template = ResourceFileUtil.readResource(SINGLE_PROMPT_PATH);
                String existingThemes = formatter.formatExistingThemes(repository.findAllThemes());

                return String.format(template, word, definition, existingThemes);
            }
}