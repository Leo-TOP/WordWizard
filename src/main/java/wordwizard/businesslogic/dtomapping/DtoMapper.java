package wordwizard.businesslogic.dtomapping;

import org.springframework.stereotype.Service;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Word;
import wordwizard.repository.externalsourcefetching.dto.ApiDefinition;
import wordwizard.repository.externalsourcefetching.dto.DictionaryApiResponse;
import wordwizard.repository.externalsourcefetching.dto.Meaning;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DtoMapper {
    public Word mapToEntity(String wordText, DictionaryApiResponse response) {
        Word word = new Word();
        word.setWord(wordText);
        word.setIsUserDefined(false);
        word.setCreatedAt(LocalDateTime.now());
        word.setUpdatedAt(LocalDateTime.now());

        List<Definition> definitions = new ArrayList<>();
        for (Meaning meaning : response.meanings()) {
            for (ApiDefinition apiDef : meaning.definitions()) {
                Definition def = new Definition();
                def.setDefinition(apiDef.definition());
                def.setPartOfSpeech(meaning.partOfSpeech());
                def.setSource("dictionaryapi.dev");
                def.setCreatedAt(LocalDateTime.now());
                def.setWord(word);

                if (apiDef.example() != null) {
                    def.setDefinition(def.getDefinition() + " [Example: " + apiDef.example() + "]");
                }

                definitions.add(def);
            }
        }

        word.setDefinitions(definitions);
        return word;
    }
}
