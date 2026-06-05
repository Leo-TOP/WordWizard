package wordwizard.repository.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wordwizard.repository.entities.Theme;
import wordwizard.repository.entities.Word;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DatabaseManager {
    public List<Theme> findAllThemes(){return null;}
    public List<Word> getAllWords() {return null;}
    public Long getOrCreateTheme(String theme) {return null;}
    public void assignThemeToDefinition(Long wordId , Long themeId ,Long defId){}

    public List<Word> findWordsForExport(String theme, String s, String s1) { return null;}
}
