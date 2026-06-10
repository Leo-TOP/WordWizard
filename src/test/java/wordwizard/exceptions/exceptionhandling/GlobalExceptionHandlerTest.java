package wordwizard.exceptions.exceptionhandling;

import org.junit.jupiter.api.Test;
import wordwizard.exceptions.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsValidationExceptionsWithPrefixes() {
        assertEquals("Unknown command. Type \"help\" to see available commands.",
                handler.handle(new CommandNotFoundException("x")));
        assertEquals("Usage error: x", handler.handle(new CommandValidationException("x")));
        assertEquals("Invalid request: x", handler.handle(new InvalidRequestException("x")));
        assertEquals("Invalid word: x", handler.handle(new InvalidWordException("x")));
        assertEquals("Invalid definition: x", handler.handle(new InvalidDefinitionException("x")));
        assertEquals("Invalid theme: x", handler.handle(new InvalidThemeException("x")));
        assertEquals("Invalid filter: x", handler.handle(new InvalidStartsWithException("x")));
    }

    @Test
    void mapsDomainResultsToTheirOwnMessage() {
        assertEquals("Word \"cat\" was not found in the dictionary",
                handler.handle(new WordNotFoundException("Word \"cat\" was not found in the dictionary")));
        assertEquals("Words were saved, but theme assignment failed: 503",
                handler.handle(new ThemeAssignmentException("Words were saved, but theme assignment failed: 503")));
    }

    @Test
    void mapsExternalServiceExceptions() {
        assertEquals("Dictionary service error: x", handler.handle(new DictionaryApiException("x")));
        assertEquals("AI service error: x", handler.handle(new GeminiApiException("x")));
        assertEquals("Embedding error: x", handler.handle(new EmbeddingException("x")));
    }

    @Test
    void subclassesResolveToNearestRegisteredParent() {
        // AiResponseParseException extends GeminiApiException
        assertEquals("AI service error: bad json", handler.handle(new AiResponseParseException("bad json")));
        // InvalidFileExtensionException extends FileException
        assertEquals("File error: bad ext", handler.handle(new InvalidFileExtensionException("bad ext")));
        // PermissionDeniedException has its own entry despite extending FileException
        assertEquals("Permission denied: no", handler.handle(new PermissionDeniedException("no")));
    }

    @Test
    void mapsExportAndFileExceptions() {
        assertEquals("Format error: x", handler.handle(new InvalidFormatException("x")));
        assertEquals("Export error: x", handler.handle(new ExportException("x")));
        assertEquals("File error: x", handler.handle(new FileException("x")));
    }

    @Test
    void dataMappingExceptionIsFlaggedAsBug() {
        String message = handler.handle(new DataMappingException("weird type"));
        assertTrue(message.startsWith("Internal data error: weird type"));
        assertTrue(message.contains("bug"));
    }

    @Test
    void batchImportExceptionMessageListsSavedAndFailedWords() {
        BatchImportException e = new BatchImportException(
                List.of("cat"), Map.of("dgo", "Word \"dgo\" was not found in the dictionary"));

        String message = handler.handle(e);

        assertTrue(message.contains("Imported 1 of 2 word(s)"));
        assertTrue(message.contains("Saved: cat"));
        assertTrue(message.contains("dgo: Word \"dgo\" was not found"));
        assertEquals(List.of("cat"), e.getImportedWords());
        assertEquals(1, e.getFailures().size());
    }

    @Test
    void unknownExceptionsFallBackWithClassName() {
        assertEquals("Unexpected error (IllegalStateException): boom",
                handler.handle(new IllegalStateException("boom")));

        // exceptions without a message must not print "null"
        String message = handler.handle(new NullPointerException());
        assertTrue(message.startsWith("Unexpected error (NullPointerException):"));
        assertTrue(message.contains("NullPointerException"));
    }
}
