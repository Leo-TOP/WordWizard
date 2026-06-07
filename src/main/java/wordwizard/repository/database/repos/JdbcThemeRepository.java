package wordwizard.repository.database.repos;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wordwizard.models.Theme;

import java.util.List;
import java.util.Optional;


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
                SELECT t.id, t.name, t.description, COUNT(DISTINCT wt.word_id) AS word_count
                FROM themes t
                LEFT JOIN word_themes wt ON t.id = wt.theme_id
                GROUP BY t.id, t.name, t.description
                ORDER BY t.name
                """,
                THEME_MAPPER
        );
    }

    public Optional<Theme> findByName(String name) {
        List<Theme> results = jdbc.query(
                "SELECT id, name, word_count FROM themes WHERE name = ?",
                THEME_MAPPER,
                name
        );

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }


    public Long getOrCreate(String name) {
        return findByName(name)
                .map(Theme::id)
                .orElseGet(() -> jdbc.queryForObject(
                        "INSERT INTO themes (name) VALUES (?) RETURNING id",
                        Long.class,
                        name
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
