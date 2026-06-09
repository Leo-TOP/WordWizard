package wordwizard.service.export.exporters;

import org.springframework.stereotype.Component;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class MarkdownExporter implements Exporter {

    @Override
    public String getFormat() {
        return "markdown";
    }

    @Override
    public byte[] export(Map<String, List<Word>> themedWords) {
        StringBuilder md = new StringBuilder("# Vocabulary Export\n\n");

        themedWords.forEach((theme, words) -> appendThemedGroup(md, theme, words));

        return md.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendThemedGroup(StringBuilder md, String theme, List<Word> words){
        md.append("## ").append(theme).append("\n\n");
        words.forEach(w -> appendWord(md, w));
        md.append("\n");
    }

    private void appendWord(StringBuilder md, Word word) {
        md.append("### ").append(word.word()).append("\n\n");
        for (Definition def : word.definitions()) {
           appendDefinition(md, def);
        }
        md.append("\n");
    }

    private void appendDefinition(StringBuilder md, Definition def) {
        md.append("- ");
        if (def.partOfSpeech() != null) {
            md.append("*").append(def.partOfSpeech()).append("* ");
        }
        md.append(def.text());
        md.append(" *(source: ").append(def.source()).append(")*");
        if (def.example() != null && !def.example().isEmpty()) {
            md.append("  \n  > Example: ").append(def.example());
        }
        md.append("\n");
    }
}
