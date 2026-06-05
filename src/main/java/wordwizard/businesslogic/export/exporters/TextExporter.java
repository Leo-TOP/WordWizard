package wordwizard.businesslogic.export.exporters;

import org.springframework.stereotype.Component;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Word;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TextExporter implements Exporter {

    @Override
    public String getFormat() {
        return "text";
    }

    @Override
    public byte[] export(List<Word> words) {
        StringBuilder sb = new StringBuilder("Vocabulary Export\n\n");

        for (Word word : words) {
            sb.append(word.word()).append("\n");
            for (Definition def : word.definitions()) {
                appendDefinition(sb, def);
            }
            sb.append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendDefinition(StringBuilder sb, Definition def) {
        sb.append("  ");
        if (def.partOfSpeech() != null && !def.partOfSpeech().isEmpty()) {
            sb.append("[").append(def.partOfSpeech()).append("] ");
        }
        sb.append(def.text());
        if (def.example() != null && !def.example().isEmpty()) {
            sb.append("\n    Example: \"").append(def.example()).append("\"");
        }
        sb.append("\n");
    }
}
