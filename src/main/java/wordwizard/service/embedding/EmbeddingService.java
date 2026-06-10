package wordwizard.service.embedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import wordwizard.exceptions.EmbeddingException;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;
    private final DatabaseManager repository;

    public float[] getEmbedding(String text) {
        try {
            return embeddingModel.embed(text);
        } catch (Exception e) {
            throw new EmbeddingException(
                    "Failed to generate embedding for \"" + text + "\": " + e.getMessage(), e);
        }
    }

    public void generateAndSaveEmbeddingsForWord(Word word) {
        List<String> failedDefinitions = new ArrayList<>();

        for (Definition def : word.definitions()) {
            if (def.embedding() != null) continue;
            try {
                generateAndSaveEmbeddingForDefinition(def);
            } catch (EmbeddingException e) {
                log.warn("Failed embedding for definition {}: {}", def.id(), e.getMessage());
                failedDefinitions.add("\"" + def.text() + "\"");
            }
        }

        if (!failedDefinitions.isEmpty()) {
            throw new EmbeddingException(
                    "Word \"" + word.word() + "\" was saved, but embeddings failed for "
                            + failedDefinitions.size() + " definition(s): "
                            + String.join(", ", failedDefinitions)
                            + ". These definitions will not appear in similarity searches.");
        }
    }

    public void generateAndSaveEmbeddingForDefinition(Definition def) {
        if (def.id() == null) {
            throw new EmbeddingException(
                    "Cannot save embedding: definition has no database id (text: \"" + def.text() + "\")");
        }

        float[] vector = getEmbedding(def.text());
        try {
            repository.updateDefinitionEmbedding(def.id(), vector);
        } catch (DataAccessException e) {
            throw new EmbeddingException(
                    "Failed to store embedding for definition " + def.id() + ": " + e.getMessage(), e);
        }
    }
}
