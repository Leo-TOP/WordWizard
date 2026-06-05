package wordwizard.businesslogic.export.exporters;

import org.springframework.stereotype.Component;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Word;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class MarkdownExporter implements Exporter {

    @Override
    public String getFormat() {
        return "markdown";
    }

    @Override
    public byte[] export(List<Word> words) {
        StringBuilder md = new StringBuilder("# Vocabulary Export\n\n");

        for (Word word : words) {
            md.append("## ").append(word.word()).append("\n\n");
            for (Definition def : word.definitions()) {
                appendDefinition(md, def);
            }
            md.append("\n");
        }

        return md.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendDefinition(StringBuilder md, Definition def) {
        md.append("- ");
        if (def.partOfSpeech() != null) {
            md.append("*").append(def.partOfSpeech()).append("* ");
        }
        md.append(def.text());
        if (def.example() != null && !def.example().isEmpty()) {
            md.append("  \n  > Example: ").append(def.example());
        }
        md.append("\n");
    }
}
