package wordwizard.businesslogic.ai.promptbuilding;

import org.springframework.stereotype.Component;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Theme;
import wordwizard.repository.entities.Word;

import java.util.List;
import java.util.stream.Collectors;

@Component
class PromptObjectFormatter {
    String formatExistingThemes(List<Theme> themes) {
        if (themes.isEmpty()) {
            return "(No existing themes yet — create new ones as needed)";
        }

        return themes.stream()
                .map(Theme::name)
                .filter(name -> name != null && !name.isBlank())
                .map(t -> "- " + t)
                .collect(Collectors.joining("\n"));
    }

    String formatWordsForPrompt(List<Word> words) {
        StringBuilder sb = new StringBuilder();
        for (Word word : words) {
            sb.append("Word: \"").append(word.word()).append("\"\n");
            List<Definition> defs = word.definitions();
            for (int i = 0; i < defs.size(); i++) {
                sb.append(formatDefinition(i, defs.get(i)));
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    String formatDefinition(int index, Definition def) {
        String pos = def.partOfSpeech();
        if (pos != null && !pos.isEmpty()) {
            return String.format("  [%d] %s (%s)\n", index, def.text(), pos);
        }
        return String.format("  [%d] %s\n", index, def.text());
    }
}
