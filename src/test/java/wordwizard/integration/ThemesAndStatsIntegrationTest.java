package wordwizard.integration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import wordwizard.exceptions.GeminiApiException;
import wordwizard.exceptions.ThemeAssignmentException;
import wordwizard.models.Theme;
import wordwizard.models.VocabularyStats;
import wordwizard.service.CentralService;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.wordsfetching.dto.WordRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThemesAndStatsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CentralService centralService;

    @Test
    void reusesExistingThemeForSecondWord() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("lion", 0, "animals"));
        centralService.addUserWords(List.of(new UserWordRequest("lion", "a big cat")));

        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("tiger", 0, "animals"));
        centralService.addUserWords(List.of(new UserWordRequest("tiger", "a striped cat")));

        assertEquals(1, countRows("themes"));
        assertEquals(2, countRows("word_themes"));
    }

    @Test
    void wrapsGeminiFailureIntoThemeAssignmentException() {
        expectDictionarySuccess("storm");
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenThrow(new GeminiApiException("Gemini API call failed: 503"));

        ThemeAssignmentException e = assertThrows(ThemeAssignmentException.class,
                () -> centralService.getWord(new WordRequest("storm", null, null)));

        assertTrue(e.getMessage().contains("Words were saved"));
        // the word itself was persisted before themes failed
        assertEquals(1, countRows("words"));
    }

    @Test
    void wrapsUnparseableAiResponseIntoThemeAssignmentException() {
        expectDictionarySuccess("mist");
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn("I am not JSON, sorry");

        ThemeAssignmentException e = assertThrows(ThemeAssignmentException.class,
                () -> centralService.getWord(new WordRequest("mist", null, null)));

        assertTrue(e.getMessage().contains("theme assignment failed"));
    }

    @Test
    void ignoresAssignmentsForUnknownWordsAndBadIndexes() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString())).thenReturn("""
                {"theme_assignments":[
                  {"word":"nobody", "definition_index":0, "theme":"ghosts"},
                  {"word":"wolf",   "definition_index":99,"theme":"animals"},
                  {"word":"wolf",   "definition_index":0, "theme":"  "},
                  {"word":"",       "definition_index":0, "theme":"animals"}
                ]}""");

        centralService.addUserWords(List.of(new UserWordRequest("wolf", "a wild dog")));

        assertEquals(0, countRows("word_themes"));
        assertEquals(0, countRows("themes"));
    }

    @Test
    void skipsAiCallWhenWordIsAlreadyFullyThemed() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("lion", 0, "animals"));
        centralService.addUserWords(List.of(new UserWordRequest("lion", "a big cat")));
        assertEquals(1, countRows("word_themes"));

        Mockito.clearInvocations(geminiChatClient);
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("lion", 0, "royalty")); // would create a second theme if used
        centralService.getWord(new WordRequest("lion", null, null));

        Mockito.verify(geminiChatClient, Mockito.never()).generateContent(Mockito.anyString());
        assertEquals(1, countRows("word_themes"));
        assertEquals(1, countRows("themes"));
    }

    @Test
    void assignsThemesOnNextLookupAfterFailedAssignment() {
        expectDictionarySuccess("storm");
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenThrow(new GeminiApiException("User location is not supported"));
        assertThrows(ThemeAssignmentException.class,
                () -> centralService.getWord(new WordRequest("storm", null, null)));
        assertEquals(0, countRows("word_themes"));

        Mockito.reset(geminiChatClient);
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("storm", 0, "weather"));
        centralService.getWord(new WordRequest("storm", null, null)); // cache hit, still unthemed

        assertEquals(1, countRows("word_themes"));
    }

    @Test
    void themesOnlyTheNewDefinitionWhenAddedToThemedWord() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("lion", 0, "animals"));
        centralService.addUserWords(List.of(new UserWordRequest("lion", "a big cat")));

        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("lion", 0, "royalty"));
        centralService.addUserWords(List.of(new UserWordRequest("lion", "a brave person")));

        assertEquals(2, countRows("word_themes"));
        assertEquals(2, countRows("themes"));
    }

    @Test
    void getAllThemesReturnsThemesWithWordCounts() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("falcon", 0, "birds"));
        centralService.addUserWords(List.of(new UserWordRequest("falcon", "a fast bird")));

        List<Theme> themes = centralService.getAllThemes();

        assertEquals(1, themes.size());
        assertEquals("birds", themes.getFirst().name());
        assertEquals(1, themes.getFirst().wordCount());
    }

    @Test
    void statisticsCountWordsDefinitionsAndThemes() {
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn(themeJson("owl", 0, "birds"));
        centralService.addUserWords(List.of(new UserWordRequest("owl", "a night bird")));
        expectDictionarySuccess("swim");
        centralService.getWord(new WordRequest("swim", null, null));

        VocabularyStats stats = centralService.getStatistics();

        assertEquals(2, stats.totalWords());
        assertEquals(3, stats.totalDefinitions()); // 1 user + 2 from dictionary
        assertEquals(1, stats.totalThemes());
        assertEquals(2, stats.wordsAddedLast7Days());
        assertEquals(2, stats.wordsAddedLast30Days());
        assertEquals(1.5, stats.avgDefinitionsPerWord(), 0.001);
        assertEquals(1, stats.wordsByTheme().get("birds"));
        assertTrue(stats.wordsByPos().containsKey("noun"));
    }

    @Test
    void statisticsOnEmptyDatabaseAreAllZero() {
        VocabularyStats stats = centralService.getStatistics();

        assertEquals(0, stats.totalWords());
        assertEquals(0, stats.totalDefinitions());
        assertEquals(0.0, stats.avgDefinitionsPerWord(), 0.001);
        assertTrue(stats.wordsByPos().isEmpty());
        assertTrue(stats.wordsByTheme().isEmpty());
    }
}
