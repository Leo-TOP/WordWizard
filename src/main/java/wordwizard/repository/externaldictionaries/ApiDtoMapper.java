package wordwizard.repository.externaldictionaries;

import org.springframework.stereotype.Component;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.externaldictionaries.dto.DictionaryApiResponse;

import java.util.List;

@Component
public class ApiDtoMapper {
    public Word mapToWord( DictionaryApiResponse response) {
        List<Definition> definitions = response.meanings().stream()
                .flatMap(meaning -> meaning.definitions().stream()
                        .map(apiDef -> Definition.create(
                                apiDef.definition(),
                                meaning.partOfSpeech(),
                                "dictionaryapi.dev",
                                apiDef.example()
                        ))
                )
                .toList();

        return Word.create(response.word(), definitions);
    }
}
