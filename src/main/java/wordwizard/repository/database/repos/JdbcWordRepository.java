package wordwizard.repository.database.repos;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import wordwizard.models.Word;
import wordwizard.repository.database.helpers.accumulaterows.AccumulateUtil;

import java.util.*;


@Repository
@RequiredArgsConstructor
public class JdbcWordRepository {

    private static final String WORD_WITH_DEFINITIONS_SQL = """
            SELECT w.id, w.word, w.created_at, w.updated_at,
                   d.id AS def_id, d.definition, d.part_of_speech, d.source, d.example
            FROM words w
            LEFT JOIN definitions d ON w.id = d.word_id
            """;

    private final JdbcTemplate jdbc;

    public Optional<Word> findByName(String word) {
        List<Map<String, Object>> rows = jdbc.queryForList(WORD_WITH_DEFINITIONS_SQL
                + " WHERE w.word = ? ORDER BY d.id", word);

        List<Word> words = AccumulateUtil.accumulateRows(rows);
        return words.isEmpty() ? Optional.empty() : Optional.of(words.getFirst());
    }

    public Map<String, List<Word>> findGroupedByTheme(String theme, String partOfSpeech, String startsWith) {
        String sql = """
                SELECT COALESCE(t.name, 'Uncategorized') AS theme_name,
                       w.id, w.word, w.created_at, w.updated_at,
                       d.id AS def_id, d.definition, d.part_of_speech, d.source, d.example
                FROM words w
                LEFT JOIN definitions d ON w.id = d.word_id
                LEFT JOIN word_themes wt ON d.id = wt.definition_id
                LEFT JOIN themes t ON wt.theme_id = t.id
                WHERE (? IS NULL OR t.name = ?)
                  AND (? IS NULL OR d.part_of_speech = ?)
                  AND (? IS NULL OR w.word ILIKE ? || '%')
                ORDER BY theme_name, w.word, d.id
                """;
        List<Map<String, Object>> rows = jdbc.queryForList(
                sql,
                theme, theme,
                partOfSpeech, partOfSpeech,
                startsWith, startsWith
        );
        return AccumulateUtil.accumulateGroupedRows(rows);
    }

    public Long save(Word word) {
        return jdbc.queryForObject(
                """
                    INSERT INTO words (word, created_at, updated_at)
                    VALUES (?, ?, ?) RETURNING id
                    """,
                Long.class,
                word.word(),
                word.createdAt(),
                word.updatedAt()
        );
    }

    public Word findById(Long wordId) {
        List<Map<String, Object>> rows = jdbc.queryForList(
                WORD_WITH_DEFINITIONS_SQL + " WHERE w.id = ? ORDER BY d.id", wordId);
        List<Word> words = AccumulateUtil.accumulateRows(rows);
        return words.isEmpty() ? null : words.getFirst();
    }

    public void updateWordUpdatedAt(Long id) {
        jdbc.update("UPDATE words SET updated_at = NOW() WHERE id = ?", id);
    }
}
