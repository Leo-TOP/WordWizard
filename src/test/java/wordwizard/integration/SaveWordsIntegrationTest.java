package wordwizard.integration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import wordwizard.exceptions.InvalidDefinitionException;
import wordwizard.exceptions.InvalidRequestException;
import wordwizard.exceptions.InvalidWordException;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.repository.database.repos.JdbcDefinitionRepository;
import wordwizard.service.CentralService;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.save.message.SaveResult;
import wordwizard.service.save.message.SaveStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveWordsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CentralService centralService;

    @Autowired
    private DatabaseManager databaseManager;

    @Autowired
    private JdbcDefinitionRepository definitionRepository;

    @Test
    void savesNewWordWithEmbedding() {
        List<SaveResult> results = centralService.addUserWords(
                List.of(new UserWordRequest("griffin", "a mythical creature")));

        assertEquals(1, results.size());
        assertEquals(SaveStatus.WORD_SAVED, results.getFirst().status());

        Word saved = databaseManager.findByWord("griffin").orElseThrow();
        assertEquals("user", saved.definitions().getFirst().source());
        Integer embedded = jdbc.queryForObject(
                "SELECT COUNT(*) FROM definitions WHERE embedding IS NOT NULL", Integer.class);
        assertEquals(1, embedded);
    }

    @Test
    void skipsDuplicateDefinition() {
        centralService.addUserWords(List.of(new UserWordRequest("cat", "a small animal")));

        List<SaveResult> second = centralService.addUserWords(
                List.of(new UserWordRequest("cat", "a small animal")));

        assertEquals(SaveStatus.DUPLICATE_SKIPPED, second.getFirst().status());
        assertEquals(1, countRows("definitions"));
    }

    @Test
    void addsNewDefinitionToExistingWord() {
        centralService.addUserWords(List.of(new UserWordRequest("cat", "a small animal")));

        List<SaveResult> second = centralService.addUserWords(
                List.of(new UserWordRequest("cat", "a person who naps all day")));

        assertEquals(SaveStatus.DEFINITION_ADDED, second.getFirst().status());
        Word word = databaseManager.findByWord("cat").orElseThrow();
        assertEquals(2, word.definitions().size());

        // also covers JdbcDefinitionRepository.findByWordId directly
        assertEquals(2, definitionRepository.findByWordId(word.id()).size());
    }

    @Test
    void reportsFailedStatusWhenEmbeddingModelCrashes() {
        Mockito.when(embeddingModel.embed("a doomed definition"))
                .thenThrow(new RuntimeException("model crashed"));

        List<SaveResult> results = centralService.addUserWords(List.of(
                new UserWordRequest("phoenix", "a doomed definition"),
                new UserWordRequest("dragon", "a healthy definition")));

        assertEquals(2, results.size());
        SaveResult failed = results.stream()
                .filter(r -> r.word().equals("phoenix")).findFirst().orElseThrow();
        SaveResult ok = results.stream()
                .filter(r -> r.word().equals("dragon")).findFirst().orElseThrow();

        assertEquals(SaveStatus.FAILED, failed.status());
        assertTrue(failed.message().contains("embeddings failed"));
        assertEquals(SaveStatus.WORD_SAVED, ok.status());
    }

    @Test
    void assignsThemesToSavedWords() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("unicorn", 0, "Mythology"));

        centralService.addUserWords(List.of(new UserWordRequest("unicorn", "a horned horse")));

        assertEquals(1, countRows("word_themes"));
        String themeName = jdbc.queryForObject("SELECT name FROM themes", String.class);
        assertEquals("mythology", themeName); // cleaned to lower case
    }

    @Test
    void rejectsInvalidInputBeforeSaving() {
        assertThrows(InvalidWordException.class, () -> centralService.addUserWords(
                List.of(new UserWordRequest("two words", "ok definition"))));
        assertThrows(InvalidDefinitionException.class, () -> centralService.addUserWords(
                List.of(new UserWordRequest("word", "   "))));
        assertThrows(InvalidRequestException.class, () -> centralService.addUserWords(List.of()));
        assertThrows(InvalidRequestException.class, () -> centralService.addUserWords(null));
        assertEquals(0, countRows("words"));
    }
}
