package wordwizard.service.save.dto;

import org.junit.jupiter.api.Test;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import static org.junit.jupiter.api.Assertions.*;

class SaveDtoMappingTest {

    private final SaveDtoMapping mapping = new SaveDtoMapping();

    @Test
    void mapsRequestToWordWithUserSourcedDefinition() {
        Word word = mapping.mapToWord(new UserWordRequest("cat", "a small animal"));

        assertEquals("cat", word.word());
        assertEquals(1, word.definitions().size());
        assertEquals("a small animal", word.definitions().getFirst().text());
        assertEquals("user", word.definitions().getFirst().source());
    }

    @Test
    void mapsDefinitionTextOnly() {
        Definition def = mapping.mapToDefinition("a small animal");
        assertNull(def.id());
        assertNull(def.partOfSpeech());
        assertEquals("user", def.source());
    }
}
