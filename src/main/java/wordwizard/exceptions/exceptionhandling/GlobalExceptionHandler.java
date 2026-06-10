package wordwizard.exceptions.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.exceptions.*;

import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;

@Slf4j
@Component
public class GlobalExceptionHandler {

    private static final Map<Class<? extends Exception>, Function<Exception, String>> HANDLERS = Map.ofEntries(
                      entry(CommandNotFoundException.class,
                    e -> "Unknown command. Type \"help\" to see available commands."),
            entry(CommandValidationException.class, prefixed("Usage error: ")),
            entry(InvalidRequestException.class,    prefixed("Invalid request: ")),
            entry(InvalidWordException.class,       prefixed("Invalid word: ")),
            entry(InvalidDefinitionException.class, prefixed("Invalid definition: ")),
            entry(InvalidThemeException.class,      prefixed("Invalid theme: ")),
            entry(InvalidStartsWithException.class, prefixed("Invalid filter: ")),

            entry(WordNotFoundException.class,    Exception::getMessage),
            entry(BatchImportException.class,     Exception::getMessage),
            entry(ThemeAssignmentException.class, Exception::getMessage),

            entry(DictionaryApiException.class, prefixed("Dictionary service error: ")),
            entry(GeminiApiException.class,     prefixed("AI service error: ")),
            entry(EmbeddingException.class,     prefixed("Embedding error: ")),

            entry(InvalidFormatException.class,    prefixed("Format error: ")),
            entry(ExportException.class,           prefixed("Export error: ")),
            entry(PermissionDeniedException.class, prefixed("Permission denied: ")),
            entry(FileException.class,             prefixed("File error: ")),

            entry(DataMappingException.class,
                    e -> "Internal data error: " + e.getMessage() +
                            " (this is a bug, not a problem with your input)")
    );

    public String handle(Exception e) {
        Function<Exception, String> handler = findHandler(e.getClass());
        if (handler != null) {
            log.debug("Handled {}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
            return handler.apply(e);
        }

        log.error("Unhandled exception reached the CLI", e);
        String details = e.getMessage() != null ? e.getMessage() : e.toString();
        return "Unexpected error (" + e.getClass().getSimpleName() + "): " + details;
    }

    private static Function<Exception, String> findHandler(Class<?> type) {
        for (Class<?> c = type; Exception.class.isAssignableFrom(c); c = c.getSuperclass()) {
            Function<Exception, String> handler = HANDLERS.get(c);
            if (handler != null) return handler;
        }
        return null;
    }

    private static Function<Exception, String> prefixed(String prefix) {
        return e -> prefix + e.getMessage();
    }
}
