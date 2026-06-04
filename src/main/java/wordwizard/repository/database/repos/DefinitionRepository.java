package wordwizard.repository.database.repos;

import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.SimilarWord;

import java.util.List;

public interface DefinitionRepository {
    Long save(Long wordId, Definition definition);

    List<Definition> findByWordId(Long wordId);

    void updateEmbedding(Long definitionId, float[] embedding);

    List<SimilarWord> findByEmbedding(float[] queryVector, int limit);


    List<SimilarWord> findSimilarTo(String excludeWord, float[] queryVector, int limit);
}
