package wordwizard.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wordwizard.models.*;
import wordwizard.repository.database.repos.JdbcStatisticsRepository;
import wordwizard.repository.database.repos.JdbcDefinitionRepository;
import wordwizard.repository.database.repos.JdbcThemeRepository;
import wordwizard.repository.database.repos.JdbcWordRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DatabaseManager {
    private final JdbcThemeRepository themeRepository;
    private final JdbcStatisticsRepository statisticsRepository;
    private final JdbcDefinitionRepository definitionRepository;
    private final JdbcWordRepository wordRepository;

    public List<Theme> findAllThemes() { return themeRepository.findAll(); }

    public Long getOrCreateTheme(String theme) { return themeRepository.getOrCreate(theme); }

    public void assignThemeToDefinition(Long wordId, Long themeId, Long defId) {
        themeRepository.assignToDefinition(wordId, themeId, defId);
    }

    public Optional<Word> findByWord(String word) { return wordRepository.findByName(word); }

    @Transactional
    public Word saveWord(Word newWord) {
        Long id = wordRepository.save(newWord);
        newWord.definitions().forEach(def -> definitionRepository.save(id, def));
        return wordRepository.findById(id);
    }

    public void updateDefinitionEmbedding(Long id, float[] vector) {
        definitionRepository.updateEmbedding(id, vector);
    }

    public List<SimilarWord> getSimilarTo(String word, float[] vector, String pos, Integer limit) {
        return definitionRepository.findSimilarTo(word, vector, pos, limit);
    }

    public List<SimilarWord> getSimilarByEmbedding(float[] vector, Integer limit) {
        return definitionRepository.findByEmbedding(vector, limit);
    }

    public Map<String, List<Word>> getAllWordsGroupedByTheme() {
        return wordRepository.findGroupedByTheme(null, null, null);
    }

    public Map<String, List<Word>> findFilteredWordsGroupedByTheme(String theme,
                                                                   String partOfSpeech,
                                                                   String startsWith) {
        return wordRepository.findGroupedByTheme(theme, partOfSpeech, startsWith);
    }

    public VocabularyStats getStatistics() { return statisticsRepository.getStatistics(); }

    public boolean findSimilarDefinition(Long wordId, float[] newEmbedding, double duplicateThreshold) {
        return definitionRepository.findSimilarDefinition(wordId, newEmbedding, duplicateThreshold);
    }

    @Transactional
    public Long addDefinitionToWord(Long id, Definition newDef) {
        Long defId = definitionRepository.save(id, newDef);
        wordRepository.updateWordUpdatedAt(id);
        return defId;
    }
}
