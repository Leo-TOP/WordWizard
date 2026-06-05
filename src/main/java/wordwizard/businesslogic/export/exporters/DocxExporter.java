package wordwizard.businesslogic.export.exporters;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Word;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class DocxExporter implements Exporter {

    @Override
    public String getFormat() {
        return "docx";
    }

    @Override
    public byte[] export(List<Word> words) {
        try (XWPFDocument doc = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            addTitle(doc, "Vocabulary Export");

            for (Word word : words) {
                addHeading(doc, word.word());
                for (Definition def : word.definitions()) {
                    addDefinitionLine(doc, def);
                }
                doc.createParagraph();
            }

            doc.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to create DOCX", e);
        }
    }

    private void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setStyle("Heading1");
        p.createRun().setText(text);
    }

    private void addHeading(XWPFDocument doc, String text) {
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
        if (def.partOfSpeech() == null) return def.text();
        return "(" + def.partOfSpeech() + ") " + def.text();
    }
}
