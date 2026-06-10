package wordwizard.integration;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import wordwizard.exceptions.ExportException;
import wordwizard.exceptions.InvalidFileExtensionException;
import wordwizard.exceptions.InvalidFormatException;
import wordwizard.exceptions.InvalidRequestException;
import wordwizard.service.CentralService;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.service.export.dto.ExportRequest;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.models.Word;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExportIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CentralService centralService;

    @TempDir
    Path tempDir;

    private void seedThemedWord() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("comet", 0, "space"));
        centralService.addUserWords(List.of(new UserWordRequest("comet", "an icy space rock")));
    }

    @Test
    void exportsMarkdownGroupedByTheme() throws Exception {
        seedThemedWord();
        Path out = tempDir.resolve("vocab.md");

        centralService.exportWords(new ExportRequest(null, "markdown", out.toString()));

        String content = Files.readString(out);
        assertTrue(content.contains("# Vocabulary Export"));
        assertTrue(content.contains("## space"));
        assertTrue(content.contains("### comet"));
        assertTrue(content.contains("an icy space rock"));
    }

    @Test
    void exportsTextIntoNestedDirectories() throws Exception {
        seedThemedWord();
        Path out = tempDir.resolve("deep").resolve("nested").resolve("vocab.txt");

        centralService.exportWords(new ExportRequest(null, "text", out.toString()));

        String content = Files.readString(out);
        assertTrue(content.contains("=== space ==="));
        assertTrue(content.contains("comet"));
    }

    @Test
    void exportsExcelWorkbookWithThemeSheets() throws Exception {
        seedThemedWord();
        Path out = tempDir.resolve("vocab.xlsx");

        centralService.exportWords(new ExportRequest(null, "excel", out.toString()));

        try (XSSFWorkbook workbook = new XSSFWorkbook(
                new ByteArrayInputStream(Files.readAllBytes(out)))) {
            assertEquals(1, workbook.getNumberOfSheets());
            assertEquals("space", workbook.getSheetAt(0).getSheetName());
            assertEquals("comet", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
        }
    }

    @Test
    void exportsDocxDocument() throws Exception {
        seedThemedWord();
        Path out = tempDir.resolve("vocab.docx");

        centralService.exportWords(new ExportRequest(null, "docx", out.toString()));

        try (XWPFDocument doc = new XWPFDocument(
                new ByteArrayInputStream(Files.readAllBytes(out)))) {
            String allText = doc.getParagraphs().stream()
                    .map(p -> p.getText()).reduce("", (a, b) -> a + "\n" + b);
            assertTrue(allText.contains("Vocabulary Export"));
            assertTrue(allText.contains("comet"));
        }
    }

    @Test
    void exportFilteredByThemeOnlyIncludesMatchingWords() throws Exception {
        seedThemedWord();
        centralService.addUserWords(List.of(new UserWordRequest("pebble", "a small stone")));
        Path out = tempDir.resolve("space.md");

        centralService.exportWords(new ExportRequest(
                new FilterRequest("space", null, null), "markdown", out.toString()));

        String content = Files.readString(out);
        assertTrue(content.contains("comet"));
        assertFalse(content.contains("pebble"));
    }

    @Test
    void filterGroupsUncategorizedWordsAndAppliesStartsWith() {
        seedThemedWord();
        centralService.addUserWords(List.of(new UserWordRequest("pebble", "a small stone")));

        Map<String, List<Word>> all = centralService.filter(new FilterRequest(null, null, null));
        assertTrue(all.containsKey("space"));
        assertTrue(all.containsKey("Uncategorized"));

        Map<String, List<Word>> filtered = centralService.filter(new FilterRequest(null, null, "peb"));
        assertEquals(1, filtered.size());
        assertEquals("pebble", filtered.get("Uncategorized").getFirst().word());
    }

    @Test
    void exportThrowsWhenNothingMatchesFilters() {
        seedThemedWord();
        Path out = tempDir.resolve("empty.md");

        ExportException e = assertThrows(ExportException.class,
                () -> centralService.exportWords(new ExportRequest(
                        new FilterRequest("nonexistent", null, null), "markdown", out.toString())));

        assertTrue(e.getMessage().contains("nothing to export"));
        assertFalse(Files.exists(out));
    }

    @Test
    void exportRejectsUnknownFormatAndWrongExtension() {
        seedThemedWord();

        assertThrows(InvalidFormatException.class,
                () -> centralService.exportWords(new ExportRequest(
                        null, "pdf", tempDir.resolve("x.pdf").toString())));

        assertThrows(InvalidFileExtensionException.class,
                () -> centralService.exportWords(new ExportRequest(
                        null, "markdown", tempDir.resolve("x.txt").toString())));

        assertThrows(InvalidRequestException.class, () -> centralService.exportWords(null));
    }
}
