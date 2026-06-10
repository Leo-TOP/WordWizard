package wordwizard.service.embedding;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.dao.DataAccessResourceFailureException;
import wordwizard.exceptions.EmbeddingException;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class EmbeddingServiceTest {

    private final EmbeddingModel model = Mockito.mock(EmbeddingModel.class);
    private final DatabaseManager repository = Mockito.mock(DatabaseManager.class);
    private final EmbeddingService service = new EmbeddingService(model, repository);

    private static final float[] VECTOR = {0.1f, 0.2f};

    @Test
    void getEmbeddingDelegatesToModel() {
        when(model.embed("text")).thenReturn(VECTOR);
        assertArrayEquals(VECTOR, service.getEmbedding("text"));
    }

    @Test
    void modelFailureIsWrappedInEmbeddingException() {
        when(model.embed("text")).thenThrow(new RuntimeException("onnx exploded"));
        EmbeddingException e = assertThrows(EmbeddingException.class,
                () -> service.getEmbedding("text"));
        assertTrue(e.getMessage().contains("onnx exploded"));
    }

    @Test
    void definitionWithoutIdIsRejected() {
        Definition unsaved = Definition.createWithOnlyText("text", "user");
        EmbeddingException e = assertThrows(EmbeddingException.class,
                () -> service.generateAndSaveEmbeddingForDefinition(unsaved));
        assertTrue(e.getMessage().contains("no database id"));
    }

    @Test
    void successfulEmbeddingIsStored() {
        when(model.embed("text")).thenReturn(VECTOR);
        Definition def = Definition.create(7L, "text", null, "user", null);

        service.generateAndSaveEmbeddingForDefinition(def);

        verify(repository).updateDefinitionEmbedding(7L, VECTOR);
    }

    @Test
    void databaseFailureIsWrappedInEmbeddingException() {
        when(model.embed("text")).thenReturn(VECTOR);
        doThrow(new DataAccessResourceFailureException("db down"))
                .when(repository).updateDefinitionEmbedding(anyLong(), any());
        Definition def = Definition.create(7L, "text", null, "user", null);

        EmbeddingException e = assertThrows(EmbeddingException.class,
                () -> service.generateAndSaveEmbeddingForDefinition(def));
        assertTrue(e.getMessage().contains("db down"));
    }

    @Test
    void definitionsWithExistingEmbeddingsAreSkipped() {
        Definition embedded = new Definition(7L, "text", null, "user", null, VECTOR);
        Word word = new Word(1L, "cat", null, null, List.of(embedded));

        service.generateAndSaveEmbeddingsForWord(word);

        verifyNoInteractions(model, repository);
    }

    @Test
    void partialFailureIsAggregatedIntoOneException() {
        when(model.embed("good def")).thenReturn(VECTOR);
        when(model.embed("bad def")).thenThrow(new RuntimeException("nope"));
        Word word = new Word(1L, "cat", null, null, List.of(
                Definition.create(1L, "good def", null, "user", null),
                Definition.create(2L, "bad def", null, "user", null)));

        EmbeddingException e = assertThrows(EmbeddingException.class,
                () -> service.generateAndSaveEmbeddingsForWord(word));

        assertTrue(e.getMessage().contains("cat"));
        assertTrue(e.getMessage().contains("1 definition(s)"));
        verify(repository).updateDefinitionEmbedding(1L, VECTOR); // the good one still saved
    }
}
