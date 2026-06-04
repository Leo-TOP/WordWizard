package wordwizard.repository.database.repos;

import wordwizard.repository.entities.Word;

import java.util.List;
import java.util.Optional;

/**
 * Persistence contract for {@link Word} entities.
 *
 * <p>Save operations only persist the {@code words} row.
 * Definitions must be saved separately through {@link DefinitionRepository}.
 * This keeps each repository's responsibility to its own table (SRP).
 */
public interface WordRepository {

    /** Find a word and all its definitions by exact name. Empty if not found. */
    Optional<Word> findByName(String word);

    /** Find multiple words by name. Unknown names are silently skipped. */
    List<Word> findByNames(List<String> names);

    /**
     * Filter words by optional criteria. {@code null} means "any".
     *
     * @param theme       exact theme name, or {@code null}
     * @param partOfSpeech  e.g. "noun", "verb", or {@code null}
     * @param searchText  substring matched against word or definition text, or {@code null}
     */
    List<Word> findFiltered(String theme, String partOfSpeech, String searchText);

    /** Return every word with all its definitions, ordered alphabetically. */
    List<Word> findAll();

    /** Return true if a word with this exact name exists. */
    boolean exists(String word);

    /**
     * Insert the word row into the database.
     *
     * @return the generated primary key
     */
    Long save(Word word);

    /** Delete a word and all its definitions (cascade handles the rest). */
    void delete(String word);
}
