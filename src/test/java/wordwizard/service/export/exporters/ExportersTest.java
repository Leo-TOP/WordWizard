package wordwizard.service.export.exporters;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExportersTest {

    private static Map<String, List<Word>> sampleData() {
        Word word = Word.createWordWithoutId("cat", List.of(
                Definition.createWithoutId("a small animal", "noun", "dict", "the cat sleeps"),
                Definition.createWithoutId("to whip", null, "dict", null)));
        Map<String, List<Word>> data = new LinkedHashMap<>();
        data.put("animals", List.of(word));
        return data;
    }

    @Test
    void markdownContainsHeadingsDefinitionsAndExamples() {
        String md = new String(new MarkdownExporter().export(sampleData()), StandardCharsets.UTF_8);

        assertTrue(md.contains("# Vocabulary Export"));
        assertTrue(md.contains("## animals"));
        assertTrue(md.contains("### cat"));
        assertTrue(md.contains("*noun* a small animal"));
        assertTrue(md.contains("> Example: the cat sleeps"));
        assertTrue(md.contains("to whip"));
    }

    @Test
    void textContainsThemeBlocksAndDefinitions() {
        String text = new String(new TextExporter().export(sampleData()), StandardCharsets.UTF_8);

        assertTrue(text.contains("=== animals ==="));
        assertTrue(text.contains("[noun] a small animal"));
        assertTrue(text.contains("Example: \"the cat sleeps\""));
    }

    @Test
    void excelCreatesSheetWithHeaderAndRows() throws Exception {
        byte[] bytes = new ExcelExporter().export(sampleData());

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            assertEquals("animals", workbook.getSheetAt(0).getSheetName());
            assertEquals("Word", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
            assertEquals("cat", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
            assertEquals("a small animal",
                    workbook.getSheetAt(0).getRow(1).getCell(2).getStringCellValue());
        }
    }

    @Test
    void excelSanitizesLongIllegalAndDuplicateSheetNames() throws Exception {
        Word word = Word.createWordWithoutId("x",
                List.of(Definition.createWithoutId("d", null, "s", null)));
        Map<String, List<Word>> data = new LinkedHashMap<>();
        String longBase = "a".repeat(35);
        data.put(longBase, List.of(word));                  // truncated to 31
        data.put(longBase + "b", List.of(word));            // truncates to the same 31 chars -> needs suffix
        data.put("[*?:/\\]", List.of(word));                // only illegal chars -> fallback name
        data.put("with[brackets]", List.of(word));          // partially cleaned

        byte[] bytes = new ExcelExporter().export(data);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            assertEquals(4, workbook.getNumberOfSheets());
            assertEquals("a".repeat(31), workbook.getSheetAt(0).getSheetName());
            assertTrue(workbook.getSheetAt(1).getSheetName().endsWith(" 2"));
            assertEquals("Theme", workbook.getSheetAt(2).getSheetName());
            assertEquals("withbrackets", workbook.getSheetAt(3).getSheetName());
        }
    }

    @Test
    void docxContainsTitleWordAndDefinitions() throws Exception {
        byte[] bytes = new DocxExporter().export(sampleData());

        try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(bytes))) {
            String allText = doc.getParagraphs().stream()
                    .map(p -> p.getText()).reduce("", (a, b) -> a + "\n" + b);
            assertTrue(allText.contains("Vocabulary Export"));
            assertTrue(allText.contains("animals"));
            assertTrue(allText.contains("cat"));
            assertTrue(allText.contains("(noun) a small animal"));
            assertTrue(allText.contains("Example: the cat sleeps"));
        }
    }
}
