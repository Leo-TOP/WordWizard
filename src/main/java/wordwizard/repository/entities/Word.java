package wordwizard.repository.entities;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Immutable domain entity representing a vocabulary word together with its definitions.
 *
 * Definitions list is always non-null (empty list if word has no definitions yet).
 * {@code id} is {@code null} for words that have not been persisted yet.
 */
public record Word(
        Long          id,
        String        word,
        boolean       isUserDefined,
        LocalDateTime createdAt,
        List<Definition> definitions
) {
    /** Compact constructor — guarantees the definitions list is always immutable and non-null. */
    public Word {
        definitions = (definitions != null)
                ? Collections.unmodifiableList(definitions)
                : List.of();
    }

    /**
     * Factory for an unsaved word (e.g. freshly fetched from an external source).
     * {@code id} is left {@code null} — it is assigned by the DB layer after insertion.
     */
    public static Word create(String word, boolean isUserDefined, List<Definition> definitions) {
        return new Word(null, word, isUserDefined, LocalDateTime.now(), definitions);
    }
}
