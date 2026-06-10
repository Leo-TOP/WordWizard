package wordwizard.repository.database.repos;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wordwizard.exceptions.DataMappingException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wordwizard.models.Theme;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class JdbcThemeRepository {
    private final JdbcTemplate jdbc;

    private static final RowMapper<Theme> THEME_MAPPER = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getLong("word_count")
    );


    public List<Theme> findAll() {
        return jdbc.query(
                """
                SELECT t.id, t.name, COUNT(DISTINCT wt.word_id) AS word_count
                FROM themes t
                LEFT JOIN word_themes wt ON t.id = wt.theme_id
                GROUP BY t.id, t.name
                ORDER BY t.name
                """,
                THEME_MAPPER
        );
    }

    public Optional<Theme> findByName(String name) {
        List<Theme> results = jdbc.query(
                "SELECT id, name, 0 AS word_count FROM themes WHERE name = ?",
                THEME_MAPPER,
                name
        );

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }


    public Long getOrCreate(String name) {
        Optional<Theme> existing = findByName(name);
        if (existing.isPresent()) return existing.get().id();

        try {
            return jdbc.queryForObject(
                    "INSERT INTO themes (name) VALUES (?) RETURNING id",
                    Long.class,
                    name
            );
        } catch (DuplicateKeyException e) {
            return findByName(name)
                    .map(Theme::id)
                    .orElseThrow(() -> new DataMappingException(
                            "Theme \"" + name + "\" is missing right after a duplicate-key conflict"));
        }
    }

    public Set<Long> findThemedDefinitionIds(List<Long> wordIds) {
        if (wordIds.isEmpty()) return Set.of();

        String placeholders = String.join(", ", Collections.nCopies(wordIds.size(), "?"));
        return new HashSet<>(jdbc.queryForList(
                "SELECT DISTINCT definition_id FROM word_themes WHERE word_id IN (" + placeholders + ")",
                Long.class,
                wordIds.toArray()
        ));
    }

    public void assignToDefinition(Long wordId, Long themeId, Long definitionId) {
        jdbc.update(
                """
                INSERT INTO word_themes (word_id, theme_id, definition_id)
                VALUES (?, ?, ?)
                ON CONFLICT (word_id, theme_id, definition_id) DO NOTHING
                """,
                wordId,
                themeId,
                definitionId
        );
    }
}
