package wordwizard.repository.externaldictionaries.dto;

import org.junit.jupiter.api.Test;
import wordwizard.models.Word;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiDtoMapperTest {

    @Test
    void flattensMeaningsIntoDefinitionsWithSourceAndPos() {
        DictionaryApiResponse response = new DictionaryApiResponse("cat", List.of(
                new Meaning("noun", List.of(
                        new ApiDefinition("a small animal", "the cat sleeps"),
                        new ApiDefinition("a cool person", null))),
                new Meaning("verb", List.of(
                        new ApiDefinition("to whip", null)))));

        Word word = new ApiDtoMapper().mapToWord(response);

        assertEquals("cat", word.word());
        assertNull(word.id());
        assertEquals(3, word.definitions().size());
        assertEquals("noun", word.definitions().get(0).partOfSpeech());
        assertEquals("verb", word.definitions().get(2).partOfSpeech());
        assertEquals("dictionaryapi.dev", word.definitions().get(0).source());
        assertEquals("the cat sleeps", word.definitions().get(0).example());
    }
}
