package wordwizard.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wordwizard.exceptions.InvalidDefinitionException;
import wordwizard.exceptions.InvalidRequestException;
import wordwizard.models.SimilarWord;
import wordwizard.service.CentralService;
import wordwizard.service.save.dto.UserWordRequest;
import wordwizard.service.similarword.dto.SimilarWordRequestForDefinition;
import wordwizard.service.similarword.dto.SimilarWordRequestForWord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimilarityIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CentralService centralService;

    private void seedWords() {
        centralService.addUserWords(List.of(
                new UserWordRequest("ocean", "a large body of salt water"),
                new UserWordRequest("mountain", "a very tall landform")));
    }

    @Test
    void findByDefinitionRanksClosestDefinitionFirst() {
        seedWords();
        // make the query embedding nearly identical to ocean's definition embedding
        customVectors.put("a huge body of water",
                TestVectors.near(TestVectors.vectorFor("a large body of salt water")));

        List<SimilarWord> results = centralService.findByDefinition(
                new SimilarWordRequestForDefinition("a huge body of water", 5));

        assertFalse(results.isEmpty());
        assertEquals("ocean", results.getFirst().word());
        assertTrue(results.getFirst().distance() < 0.1);
    }

    @Test
    void findByDefinitionWorksWithoutExplicitLimit() {
        seedWords();

        // regression test: a null limit used to crash with an unboxing NPE
        List<SimilarWord> results = centralService.findByDefinition(
                new SimilarWordRequestForDefinition("something watery", null));

        assertEquals(2, results.size());
    }

    @Test
    void findByDefinitionRespectsLimit() {
        seedWords();

        List<SimilarWord> results = centralService.findByDefinition(
                new SimilarWordRequestForDefinition("anything at all", 1));

        assertEquals(1, results.size());
    }

    @Test
    void findSimilarExcludesTheQueriedWordItself() {
        seedWords();

        List<SimilarWord> results = centralService.findSimilar(
                new SimilarWordRequestForWord("ocean", null, null));

        assertTrue(results.stream().noneMatch(r -> r.word().equals("ocean")));
        assertEquals(1, results.size()); // only "mountain" remains
    }

    @Test
    void findSimilarFiltersByPartOfSpeech() {
        seedWords(); // user definitions have NULL part_of_speech

        List<SimilarWord> results = centralService.findSimilar(
                new SimilarWordRequestForWord("ocean", 5, "noun"));

        assertTrue(results.isEmpty());
    }

    @Test
    void rejectsInvalidRequests() {
        assertThrows(InvalidRequestException.class, () -> centralService.findSimilar(null));
        assertThrows(InvalidRequestException.class, () -> centralService.findByDefinition(null));
        assertThrows(InvalidDefinitionException.class, () -> centralService.findByDefinition(
                new SimilarWordRequestForDefinition("  ", 5)));
    }
}
