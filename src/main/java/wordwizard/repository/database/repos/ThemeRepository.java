package wordwizard.repository.database.repos;

import wordwizard.repository.entities.Theme;

import java.util.List;
import java.util.Optional;

/**
 * Persistence contract for {@link Theme} entities and theme–word associations.
 */
public interface ThemeRepository {

    /** Return all themes with their word counts, ordered alphabetically. */
    List<Theme> findAll();

    /** Look up a theme by exact name. */
    Optional<Theme> findByName(String name);

    /**
     * Return the ID for an existing theme, or create it and return the new ID.
     * Idempotent — safe to call multiple times with the same name.
     */
    Long getOrCreate(String name);

    /**
     * Tag a specific definition (and its parent word) with a theme.
     * Duplicate assignments are silently ignored.
     */
    void assignToDefinition(Long wordId, Long themeId, Long definitionId);
}
