package wordwizard.repository.database.repos;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.time.LocalDateTime;
import java.util.*;


@Repository
@RequiredArgsConstructor
public class JdbcWordRepository {
    private final JdbcTemplate jdbc;

    public Optional<Word> findByName(String word) {
        List<Map<String, Object>> rows = jdbc.queryForList(WORD_WITH_DEFINITIONS_SQL + " WHERE w.word = ? ORDER BY d.id", word);
        List<Word> words = accumulateRows(rows);
        return words.isEmpty() ? Optional.empty() : Optional.of(words.getFirst());
    }

    public List<Word> findByNames(List<String> names) {
        if (names == null || names.isEmpty()) return List.of();
        List<Map<String, Object>> rows = jdbc.queryForList(
                WORD_WITH_DEFINITIONS_SQL + " WHERE w.word = ANY(?) ORDER BY w.word, d.id",
                (Object) names.toArray(new String[0])
        );
        return accumulateRows(rows);
    }

    public List<Word> findFiltered(String theme, String partOfSpeech, String searchText) {
        String sql = """
                SELECT w.id, w.word, w.is_user_defined, w.created_at,
                       d.id AS def_id, d.definition, d.part_of_speech, d.source, d.example
                FROM words w
                JOIN definitions d ON w.id = d.word_id
                LEFT JOIN word_themes wt ON d.id = wt.definition_id
                LEFT JOIN themes t ON wt.theme_id = t.id
                WHERE (? IS NULL OR t.name = ?)
                  AND (? IS NULL OR d.part_of_speech = ?)
                  AND (? IS NULL OR w.word ILIKE '%' || ? || '%'
                               OR d.definition ILIKE '%' || ? || '%')
                ORDER BY w.word, d.id
                """;
        List<Map<String, Object>> rows = jdbc.queryForList(
                sql,
                theme, theme,
                partOfSpeech, partOfSpeech,
                searchText, searchText, searchText
        );
        return accumulateRows(rows);
    }

    public List<Word> findAll() {
        List<Map<String, Object>> rows = jdbc.queryForList(
                WORD_WITH_DEFINITIONS_SQL + " ORDER BY w.word, d.id"
        );
        return accumulateRows(rows);
    }

    public boolean exists(String word) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM words WHERE word = ?", Integer.class, word);
        return count != null && count > 0;
    }

    // ------------------------------------------------------------------ //
    //  Write operations                                                    //
    // ------------------------------------------------------------------ //

    public Long save(Word word) {
        return jdbc.queryForObject(
                "INSERT INTO words (word, is_user_defined) VALUES (?, ?) RETURNING id",
                Long.class,
                word.word(),
                word.isUserDefined()
        );
    }

    // ------------------------------------------------------------------ //
    //  Private helpers                                                     //
    // ------------------------------------------------------------------ //

    /** Base SELECT clause reused by all word+definition queries. */
    private static final String WORD_WITH_DEFINITIONS_SQL = """
            SELECT w.id, w.word, w.is_user_defined, w.created_at,
                   d.id AS def_id, d.definition, d.part_of_speech, d.source, d.example
            FROM words w
            LEFT JOIN definitions d ON w.id = d.word_id
            """;

    /**
     * Groups flat JOIN rows into a list of immutable {@link Word} records.
     *
     * Uses {@link WordAccumulator} to collect {@link Definition} objects for
     * each word ID before calling {@code build()}, ensuring that the immutable
     * record is constructed only once per word.
     */
    private static List<Word> accumulateRows(List<Map<String, Object>> rows) {
        // LinkedHashMap preserves insertion order → alphabetical if SQL is ordered
        Map<Long, WordAccumulator> accumulators = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Long wordId = toLong(row.get("id"));
            WordAccumulator acc = accumulators.computeIfAbsent(wordId, id -> new WordAccumulator(row));

            Long defId = toLong(row.get("def_id"));
            if (defId != null) {
                acc.addDefinition(new Definition(
                        defId,
                        (String) row.get("definition"),
                        (String) row.get("part_of_speech"),
                        (String) row.get("example"),
                        null
                ));
            }
        }

        return accumulators.values().stream()
                .map(WordAccumulator::build)
                .toList();
    }

    private static Long toLong(Object value) {
        if (value instanceof Long l) return l;
        if (value instanceof Integer i) return i.longValue();
        return null;
    }

    private static LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
        return null;
    }

    // ------------------------------------------------------------------ //
    //  Inner builder — used only during row accumulation                  //
    // ------------------------------------------------------------------ //

    /**
     * Mutable accumulator that collects {@link Definition} rows for a single word,
     * then produces an immutable {@link Word} record.
     *
     * This pattern lets us run a single JOIN query (no N+1) while still returning
     * immutable domain objects to callers.
     */
    private static final class WordAccumulator {
        private final Long          id;
        private final String        word;
        private final boolean       isUserDefined;
        private final LocalDateTime createdAt;
        private final List<Definition> definitions = new ArrayList<>();

        WordAccumulator(Map<String, Object> row) {
            this.id            = toLong(row.get("id"));
            this.word          = (String) row.get("word");
            this.isUserDefined = Boolean.TRUE.equals(row.get("is_user_defined"));
            this.createdAt     = toLocalDateTime(row.get("created_at"));
        }

        void addDefinition(Definition definition) {
            definitions.add(definition);
        }

        Word build() {
            return new Word(id, word, isUserDefined, createdAt, definitions);
        }
    }
}
