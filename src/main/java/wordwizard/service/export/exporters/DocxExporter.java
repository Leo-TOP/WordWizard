package wordwizard.service.export.exporters;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;
import wordwizard.exceptions.ExportException;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class DocxExporter implements Exporter {

    @Override
    public String getFormat() {
        return "docx";
    }

    @Override
    public byte[] export(Map<String, List<Word>> themedWords) {
        try (XWPFDocument doc = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            addTitle(doc, "Vocabulary Export");

            for (var entry : themedWords.entrySet()) {
                addHeading1(doc, entry.getKey());
                for (Word word : entry.getValue()) {
                    addWord(doc, word);
                }
            }

            doc.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new ExportException("Failed to create DOCX file: " + e.getMessage(), e);
        }
    }

    private void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Heading1");
        p.createRun().setText(text);
    }


    private void addHeading1(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Heading1");
        p.createRun().setText(text);
    }

    private void addWord(XWPFDocument doc, Word word){
        addHeading2(doc, word.word());
        for (Definition def : word.definitions()) {
            addDefinitionLine(doc, def);
        }
        doc.createParagraph();
    }

    private void addHeading2(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Heading2");
        p.createRun().setText(text);
    }

    private void addDefinitionLine(XWPFDocument doc, Definition def) {
        XWPFParagraph defPara = doc.createParagraph();
        XWPFRun run = defPara.createRun();
        run.setText(formatDefinitionText(def));

        if (def.example() != null && !def.example().isEmpty()) {
            XWPFParagraph examplePara = doc.createParagraph();
            examplePara.setIndentationLeft(300);
            examplePara.createRun().setText("Example: " + def.example());
        }
    }

    private String formatDefinitionText(Definition def) {
        StringBuilder sb = new StringBuilder();
        if (def.partOfSpeech() != null && !def.partOfSpeech().isEmpty()) {
            sb.append("(").append(def.partOfSpeech()).append(") ");
        }

        sb.append(def.text());
        sb.append("  (source: ").append(def.source()).append(")");

        return sb.toString();
    }
}
