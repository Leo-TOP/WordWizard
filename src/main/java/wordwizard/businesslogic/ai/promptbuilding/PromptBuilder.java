package wordwizard.businesslogic.ai.promptbuilding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Theme;
import wordwizard.repository.entities.Word;
import wordwizard.util.FileUtil;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromptBuilder {
            private final PromptObjectFormatter formatter;

            private static final String BATCH_PROMPT_PATH = "prompts/theme-assignment-batch.txt";
            private static final String SINGLE_PROMPT_PATH = "prompts/theme-assignment-single.txt";


            public String buildThemeAssignmentPrompt(List<Word> words, List<Theme> allThemes) {
                String template = FileUtil.readResource(BATCH_PROMPT_PATH);
                String existingThemes = formatter.formatExistingThemes(allThemes);
                String wordDefinitions = formatter.formatWordsForPrompt(words);

                return String.format(template, existingThemes, wordDefinitions);
            }


            public String buildSingleWordPrompt(Word word, Definition def, List<Theme> allThemes) {
                String template = FileUtil.readResource(SINGLE_PROMPT_PATH);
                String formattedThemes = formatter.formatExistingThemes(allThemes);
                String formattedDef = formatter.formatDefinition(def);

                return String.format(template, word.word(), formattedDef, formattedThemes);
            }
}