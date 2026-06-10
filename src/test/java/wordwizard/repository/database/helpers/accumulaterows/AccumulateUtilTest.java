package wordwizard.repository.database.helpers.accumulaterows;

import org.junit.jupiter.api.Test;
import wordwizard.exceptions.DataMappingException;
import wordwizard.models.Word;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccumulateUtilTest {

    private static Map<String, Object> row(Long id, String word, Long defId, String definition) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", id);
        row.put("word", word);
        row.put("created_at", Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 12, 0)));
        row.put("updated_at", Timestamp.valueOf(LocalDateTime.of(2026, 1, 2, 12, 0)));
        row.put("def_id", defId);
        row.put("definition", definition);
        row.put("part_of_speech", "noun");
        row.put("source", "test");
        row.put("example", null);
        return row;
    }

    @Test
    void accumulatesMultipleDefinitionRowsIntoOneWord() {
        List<Word> words = AccumulateUtil.accumulateRows(List.of(
                row(1L, "cat", 10L, "a small animal"),
                row(1L, "cat", 11L, "to whip")));

        assertEquals(1, words.size());
        Word cat = words.getFirst();
        assertEquals("cat", cat.word());
        assertEquals(2, cat.definitions().size());
        assertEquals(LocalDateTime.of(2026, 1, 1, 12, 0), cat.createdAt());
    }

    @Test
    void wordWithoutDefinitionsGetsEmptyList() {
        List<Word> words = AccumulateUtil.accumulateRows(List.of(row(1L, "bare", null, null)));

        assertEquals(1, words.size());
        assertTrue(words.getFirst().definitions().isEmpty());
    }

    @Test
    void integerIdsAreAccepted() {
        Map<String, Object> row = row(null, "cat", null, null);
        row.put("id", 1); // driver may return Integer instead of Long
        row.put("def_id", 10);
        row.put("definition", "a small animal");

        List<Word> words = AccumulateUtil.accumulateRows(List.of(row));

        assertEquals(1L, words.getFirst().id());
        assertEquals(10L, words.getFirst().definitions().getFirst().id());
    }

    @Test
    void nullWordIdFailsLoudly() {
        assertThrows(DataMappingException.class,
                () -> AccumulateUtil.accumulateRows(List.of(row(null, "ghost", null, null))));
    }

    @Test
    void groupedRowsAreBucketedByThemeName() {
        Map<String, Object> catRow = row(1L, "cat", 10L, "a small animal");
        catRow.put("theme_name", "animals");
        Map<String, Object> stoneRow = row(2L, "stone", 20L, "a rock");
        stoneRow.put("theme_name", "Uncategorized");

        Map<String, List<Word>> grouped =
                AccumulateUtil.accumulateGroupedRows(List.of(catRow, stoneRow));

        assertEquals(2, grouped.size());
        assertEquals("cat", grouped.get("animals").getFirst().word());
        assertEquals("stone", grouped.get("Uncategorized").getFirst().word());
    }
}
