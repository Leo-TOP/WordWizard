package wordwizard.service.export.exporters;

import org.springframework.stereotype.Component;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class TextExporter implements Exporter {
    @Override
    public String getFormat() {
        return "text";
    }

    @Override
    public byte[] export(Map<String, List<Word>> themedWords) {
        StringBuilder sb = new StringBuilder("Vocabulary Export\n\n");

        themedWords.forEach((theme, words) -> appendThemedGroup(sb, theme, words));

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendThemedGroup(StringBuilder sb, String theme , List<Word> words) {
        sb.append("=== ").append(theme).append(" ===\n\n");
        words.forEach(w -> appendWord(sb, w));
        sb.append("\n");
    }

    private void appendWord(StringBuilder sb, Word word) {
        sb.append(word.word()).append("\n");
        for (Definition def : word.definitions()) {
            appendDefinition(sb, def);
        }
        sb.append("\n");
    }

    private void appendDefinition(StringBuilder sb, Definition def) {
        sb.append("  ");
        if (def.partOfSpeech() != null && !def.partOfSpeech().isEmpty()) {
            sb.append("[").append(def.partOfSpeech()).append("] ");
        }

        sb.append(def.text());
        sb.append("  (source: ").append(def.source()).append(")");
        if (def.example() != null && !def.example().isEmpty()) {
            sb.append("\n    Example: \"").append(def.example()).append("\"");
        }
        sb.append("\n");
    }
}
