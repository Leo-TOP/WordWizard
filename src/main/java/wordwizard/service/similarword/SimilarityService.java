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
public class SimilarityService {
    private static final double DUPLICATE_THRESHOLD = 0.1;
    private static final int DEFAULT_LIMIT = 10;

    private final DatabaseManager dbManager;
    private final EmbeddingService embeddingService;

    public List<SimilarWord> findSimilarToWord(SimilarWordRequestForWord request) {
        float[] vector = embeddingService.getEmbedding(request.word());
        return dbManager.getSimilarTo(request.word(), vector,
                request.partOfSpeech(), limitOrDefault(request.limit()));
    }

    public List<SimilarWord> findByDescription(SimilarWordRequestForDefinition request) {
        float[] vector = embeddingService.getEmbedding(request.definition());
        return dbManager.getSimilarByEmbedding(vector, limitOrDefault(request.limit()));
    }

    public boolean isDuplicateDefinition(float[] vector, Long word_id){
        return dbManager.findSimilarDefinition(
                word_id, vector, DUPLICATE_THRESHOLD
        );
    }

    private static int limitOrDefault(Integer limit) {
        return limit != null ? limit : DEFAULT_LIMIT;
    }
}
