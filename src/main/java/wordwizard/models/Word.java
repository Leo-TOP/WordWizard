package wordwizard.models;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record Word(
        Long          id,
        String        word,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<Definition> definitions
) {
    public Word {
        definitions = (definitions != null)
                ? Collections.unmodifiableList(definitions)
                : List.of();
    }

    public static Word createWordWithoutId(String word, List<Definition> definitions) {
        return new Word(null, word, LocalDateTime.now(), LocalDateTime.now(), definitions);
    }
}
