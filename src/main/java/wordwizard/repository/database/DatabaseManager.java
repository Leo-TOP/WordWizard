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
}
