package wordwizard.service.embedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.DatabaseManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;
    private final DatabaseManager repository;

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }

    public void generateAndSaveEmbeddings(Word word) {
        for (Definition def : word.definitions()) {
            if (def.embedding() != null) continue;
            try {
                float[] vector = getEmbedding(def.text());
                repository.updateDefinitionEmbedding(def.id(), vector);
            } catch (Exception e) {
                log.warn("Failed embedding for definition {}: {}", def.id(), e.getMessage());
            }
        }
    }
}
