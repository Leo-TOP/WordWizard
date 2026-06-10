package wordwizard.service.export;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wordwizard.exceptions.FileException;
import wordwizard.exceptions.InvalidFileExtensionException;
import wordwizard.exceptions.InvalidFormatException;
import wordwizard.service.export.exporters.DocxExporter;
import wordwizard.service.export.exporters.ExcelExporter;
import wordwizard.service.export.exporters.MarkdownExporter;
import wordwizard.service.export.exporters.TextExporter;
import wordwizard.service.export.fileprocessing.ExportFileProcessor;
import wordwizard.service.export.fileprocessing.ExportFormat;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExportUnitTest {

    private final ExporterFactory factory = new ExporterFactory(java.util.List.of(
            new MarkdownExporter(), new TextExporter(), new ExcelExporter(), new DocxExporter()));

    @TempDir
    Path tempDir;

    // ---------- ExportFormat ----------

    @Test
    void formatFromStringIsCaseInsensitive() {
        assertEquals(ExportFormat.DOCX, ExportFormat.fromString("DOCX"));
        assertEquals(ExportFormat.MARKDOWN, ExportFormat.fromString("markdown"));
    }

    @Test
    void unknownFormatThrowsListingSupported() {
        InvalidFormatException e = assertThrows(InvalidFormatException.class,
                () -> ExportFormat.fromString("pdf"));
        assertTrue(e.getMessage().contains("pdf"));
        assertTrue(e.getMessage().contains("markdown"));
    }

    @Test
    void supportsExtensionMatchesValidOnes() {
        assertTrue(ExportFormat.EXCEL.supportsExtension(".xlsx"));
        assertFalse(ExportFormat.EXCEL.supportsExtension(".md"));
    }

    // ---------- ExporterFactory ----------

    @Test
    void factoryReturnsExporterByFormatKey() {
        assertEquals("markdown", factory.getExporter("MARKDOWN").getFormat());
        assertEquals("excel", factory.getExporter("excel").getFormat());
    }

    @Test
    void factoryRejectsUnknownNullAndBlankFormats() {
        assertThrows(InvalidFormatException.class, () -> factory.getExporter("pdf"));
        assertThrows(InvalidFormatException.class, () -> factory.getExporter(null));
        assertThrows(InvalidFormatException.class, () -> factory.getExporter("  "));
    }

    // ---------- ExportFileProcessor ----------

    @Test
    void processAcceptsValidPathAndCreatesParentDirectories() {
        Path target = tempDir.resolve("a").resolve("b").resolve("out.md");

        Path result = new ExportFileProcessor(target.toString(), "markdown").process();

        assertEquals(target, result);
        assertTrue(Files.isDirectory(target.getParent()));
    }

    @Test
    void missingExtensionThrows() {
        InvalidFileExtensionException e = assertThrows(InvalidFileExtensionException.class,
                () -> new ExportFileProcessor(tempDir.resolve("noext").toString(), "text").process());
        assertTrue(e.getMessage().contains("no extension"));
    }

    @Test
    void mismatchedExtensionThrows() {
        InvalidFileExtensionException e = assertThrows(InvalidFileExtensionException.class,
                () -> new ExportFileProcessor(tempDir.resolve("out.pdf").toString(), "markdown").process());
        assertTrue(e.getMessage().contains(".pdf"));
    }

    @Test
    void nullAndBlankPathsThrowFileException() {
        assertThrows(FileException.class, () -> new ExportFileProcessor(null, "markdown"));
        assertThrows(FileException.class, () -> new ExportFileProcessor("   ", "markdown"));
    }

    @Test
    void pathWithoutFileNameThrows() {
        assertThrows(InvalidFileExtensionException.class,
                () -> new ExportFileProcessor("/", "markdown").process());
    }

    @Test
    void illegalCharactersInPathThrowFileException() {
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("win"));
        FileException e = assertThrows(FileException.class,
                () -> new ExportFileProcessor("out|put.md", "markdown"));
        assertTrue(e.getMessage().contains("Invalid output path"));
    }
}
