package wordwizard.repository.database;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wordwizard.models.SimilarWord;
import wordwizard.service.dictrequesting.dto.FilterRequest;
import wordwizard.models.Theme;
import wordwizard.models.Word;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DatabaseManager {
    public List<Theme> findAllThemes(){return null;}

    public List<Word> getAllWords() {return null;}

    public Long getOrCreateTheme(String theme) {return null;}

    public void assignThemeToDefinition(Long wordId , Long themeId ,Long defId){}

    public List<Word> findWordsWithFiltering(FilterRequest filterRequest) { return null;}

    public Optional<Word> findByWord(String word) {return Optional.empty();}

    public Word saveWord(Word newWord) {return null;}

    public void updateDefinitionEmbedding(Long id, float[] vector) {}

    public List<SimilarWord> getSimilarTo(String word, float[] vector, String s, Integer limit) {return null;}

    public List<String> getSimilarByEmbedding(float[] vector, Integer limit) { return null; }
}
