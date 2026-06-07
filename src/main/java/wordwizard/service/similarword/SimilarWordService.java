package wordwizard.service.similarword;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wordwizard.models.SimilarWord;
import wordwizard.repository.database.DatabaseManager;
import wordwizard.service.embedding.EmbeddingService;
import wordwizard.service.similarword.dto.SimilarWordRequestForDefinition;
import wordwizard.service.similarword.dto.SimilarWordRequestForWord;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SimilarWordService {
    private final DatabaseManager dbManager;
    private final EmbeddingService embeddingService;

    public List<SimilarWord> findSimilarToWord(SimilarWordRequestForWord request) {
        float[] vector = embeddingService.getEmbedding(request.word());
        return dbManager.getSimilarTo(request.word(), vector, request.partOfSpeech(), request.limit());
    }

    public List<String> findByDescription(SimilarWordRequestForDefinition request) {
        float[] vector = embeddingService.getEmbedding(request.definition());
        return dbManager.getSimilarByEmbedding(vector, request.limit());
    }
}
