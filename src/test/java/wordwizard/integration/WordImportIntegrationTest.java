package wordwizard.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import wordwizard.exceptions.BatchImportException;
import wordwizard.exceptions.DictionaryApiException;
import wordwizard.exceptions.InvalidRequestException;
import wordwizard.exceptions.InvalidWordException;
import wordwizard.exceptions.WordNotFoundException;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.service.CentralService;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordImportIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CentralService centralService;

    @Autowired
    private DatabaseManager databaseManager;

    @Test
    void getWordImportsFromDictionaryAndStoresEmbeddings() {
        expectDictionarySuccess("serendipity");

        Word word = centralService.getWord(new WordRequest("serendipity", null, null));

        assertEquals("serendipity", word.word());
        assertEquals(2, word.definitions().size());
        assertNotNull(word.id());

        assertTrue(databaseManager.findByWord("serendipity").isPresent());
        Integer embedded = jdbc.queryForObject(
                "SELECT COUNT(*) FROM definitions WHERE embedding IS NOT NULL", Integer.class);
        assertEquals(2, embedded);
    }

    @Test
    void getWordReturnsCachedWordWithoutCallingApi() {
        expectDictionarySuccess("cache");
        centralService.getWord(new WordRequest("cache", null, null));

        // no further dictionary expectation: a second HTTP call would fail the mock server
        Word again = centralService.getWord(new WordRequest("cache", null, null));

        assertEquals("cache", again.word());
        assertEquals(1, countRows("words"));
        dictionaryServer.verify();
    }

    @Test
    void getWordFiltersByPartOfSpeechAndLimit() {
        expectDictionarySuccess("run");

        Word word = centralService.getWord(new WordRequest("run", 1, "noun"));

        assertEquals(1, word.definitions().size());
        assertEquals("noun", word.definitions().getFirst().partOfSpeech());
        // full word stays in the DB regardless of display filters
        assertEquals(2, countRows("definitions"));
    }

    @Test
    void getWordThrowsWhenNoDefinitionsMatchPartOfSpeech() {
        expectDictionarySuccess("blue");

        WordNotFoundException e = assertThrows(WordNotFoundException.class,
                () -> centralService.getWord(new WordRequest("blue", null, "adjective")));

        assertTrue(e.getMessage().contains("adjective"));
    }

    @Test
    void getWordThrowsWordNotFoundOnHttp404() {
        expectDictionaryStatus("qwertzu", HttpStatus.NOT_FOUND);

        WordNotFoundException e = assertThrows(WordNotFoundException.class,
                () -> centralService.getWord(new WordRequest("qwertzu", null, null)));

        assertTrue(e.getMessage().contains("qwertzu"));
        assertEquals(0, countRows("words"));
    }

    @Test
    void getWordThrowsDictionaryApiExceptionOnServerError() {
        expectDictionaryStatus("broken", HttpStatus.INTERNAL_SERVER_ERROR);

        DictionaryApiException e = assertThrows(DictionaryApiException.class,
                () -> centralService.getWord(new WordRequest("broken", null, null)));

        assertTrue(e.getMessage().contains("broken"));
    }

    @Test
    void getWordThrowsDictionaryApiExceptionOnMalformedJson() {
        expectDictionaryBody("garbled", "{{{ not json");

        assertThrows(DictionaryApiException.class,
                () -> centralService.getWord(new WordRequest("garbled", null, null)));
    }

    @Test
    void getWordThrowsDictionaryApiExceptionOnEmptyArray() {
        expectDictionaryBody("hollow", "[]");

        DictionaryApiException e = assertThrows(DictionaryApiException.class,
                () -> centralService.getWord(new WordRequest("hollow", null, null)));

        assertTrue(e.getMessage().contains("empty"));
    }

    @Test
    void getWordRejectsInvalidCharactersBeforeAnyApiCall() {
        assertThrows(InvalidWordException.class,
                () -> centralService.getWord(new WordRequest("not a word 7", null, null)));
        dictionaryServer.verify(); // zero expectations, zero calls
    }

    @Test
    void getWordRejectsNullRequest() {
        assertThrows(InvalidRequestException.class, () -> centralService.getWord(null));
    }

    @Test
    void getWordsImportsAllWordsAndDeduplicates() {
        expectDictionarySuccess("alpha");
        expectDictionarySuccess("beta");

        List<Word> words = centralService.getWords(List.of("alpha", "beta", "alpha"));

        assertEquals(2, words.size());
        assertEquals(2, countRows("words"));
    }

    @Test
    void getWordsReportsPartialFailuresViaBatchImportException() {
        expectDictionarySuccess("good");
        expectDictionaryStatus("missing", HttpStatus.NOT_FOUND);

        BatchImportException e = assertThrows(BatchImportException.class,
                () -> centralService.getWords(List.of("good", "missing")));

        assertEquals(List.of("good"), e.getImportedWords());
        assertTrue(e.getFailures().containsKey("missing"));
        assertTrue(e.getMessage().contains("Imported 1 of 2"));
        assertTrue(e.getMessage().contains("missing"));
        // the successful word is still persisted
        assertTrue(databaseManager.findByWord("good").isPresent());
    }

    @Test
    void getWordsRejectsEmptyAndNullLists() {
        assertThrows(InvalidRequestException.class, () -> centralService.getWords(List.of()));
        assertThrows(InvalidRequestException.class, () -> centralService.getWords(null));
    }
}
