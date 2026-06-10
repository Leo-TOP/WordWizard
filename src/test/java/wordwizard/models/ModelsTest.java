package wordwizard.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelsTest {

    @Test
    void wordReplacesNullDefinitionsWithEmptyList() {
        Word word = new Word(1L, "cat", LocalDateTime.now(), LocalDateTime.now(), null);
        assertNotNull(word.definitions());
        assertTrue(word.definitions().isEmpty());
    }

    @Test
    void wordDefinitionsAreUnmodifiable() {
        Word word = new Word(1L, "cat", LocalDateTime.now(), LocalDateTime.now(),
                new ArrayList<>(List.of(Definition.createWithOnlyText("a small animal", "user"))));
        assertThrows(UnsupportedOperationException.class,
                () -> word.definitions().add(Definition.createWithOnlyText("x", "user")));
    }

    @Test
    void createWordWithoutIdSetsTimestamps() {
        Word word = Word.createWordWithoutId("cat", List.of());
        assertNull(word.id());
        assertNotNull(word.createdAt());
        assertNotNull(word.updatedAt());
    }

    @Test
    void definitionFactoriesFillExpectedFields() {
        Definition full = Definition.create(1L, "text", "noun", "src", "ex");
        assertEquals(1L, full.id());
        assertNull(full.embedding());

        Definition withoutId = Definition.createWithoutId("text", "noun", "src", "ex");
        assertNull(withoutId.id());
        assertEquals("noun", withoutId.partOfSpeech());

        Definition textOnly = Definition.createWithOnlyText("text", "user");
        assertNull(textOnly.partOfSpeech());
        assertEquals("user", textOnly.source());
    }
}
