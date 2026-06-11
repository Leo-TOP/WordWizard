package wordwizard.repository.database.repos;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wordwizard.models.VocabularyStats;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcStatisticsRepository {

    private final JdbcTemplate jdbc;

    public VocabularyStats getStatistics() {
        int totalWords = countTotalWords();
        int totalDefs  = countTotalDefinitions();
        long avgDefs = totalWords > 0 ? Math.round((double) totalDefs / totalWords) : 0;

        int totalThemes = countTotalThemes();
        int last7       = countWordsAddedSinceDays(7);
        int last30      = countWordsAddedSinceDays(30);

        Map<String, Integer> byPos   = getWordsByPos();
        Map<String, Integer> byTheme = getWordsByTheme();

        return new VocabularyStats(
                totalWords, totalDefs, avgDefs,
                byPos, byTheme,
                totalThemes, last7, last30
        );
    }

    private int countTotalWords() {
        return count("SELECT COUNT(*) FROM words");
    }

    private int countTotalDefinitions() {
        return count("SELECT COUNT(*) FROM definitions");
    }

    private int countTotalThemes() {
        return count("SELECT COUNT(*) FROM themes");
    }

    private int countWordsAddedSinceDays(int days) {
        return count("""
            SELECT COUNT(*)
            FROM words
            WHERE created_at >= NOW() - INTERVAL '%d days'
            """.formatted(days));
    }

    private Map<String, Integer> getWordsByPos() {
        String sql = """
            SELECT part_of_speech, COUNT(*) AS cnt
            FROM definitions
            WHERE part_of_speech IS NOT NULL
            GROUP BY part_of_speech
            ORDER BY cnt DESC
            """;
        return queryForOrderedMap(sql);
    }

    private Map<String, Integer> getWordsByTheme() {
        String sql = """
            SELECT t.name, COUNT(DISTINCT wt.word_id) AS cnt
            FROM themes t
            JOIN word_themes wt ON t.id = wt.theme_id
            GROUP BY t.name
            ORDER BY cnt DESC
            """;
        return queryForOrderedMap(sql);
    }

    private int count(String sql, Object... params) {
        Integer value = jdbc.queryForObject(sql, Integer.class, params);
        return value != null ? value : 0;
    }

    private Map<String, Integer> queryForOrderedMap(String sql, Object... params) {
        RowMapper<Map.Entry<String, Integer>> mapper = (rs, rowNum) ->
                Map.entry(rs.getString(1), rs.getInt(2));

        return jdbc.query(sql, mapper, params)
                .stream()
                .collect(LinkedHashMap::new,
                        (map, e) -> map.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll);
    }
}