package wordwizard.repository.externaldictionaries.dto;

import org.springframework.stereotype.Component;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.util.List;

@Component
public class ApiDtoMapper {
    public Word mapToWord( DictionaryApiResponse response) {
        List<Definition> definitions = response.meanings().stream()
                .flatMap(meaning -> meaning.definitions().stream()
                        .map(apiDef -> mapToDefinition(apiDef,
                                meaning.partOfSpeech()))
                )
                .toList();

        return Word.createWordWithoutId(response.word(), definitions);
    }

    private Definition mapToDefinition(ApiDefinition apiDef, String pos){
        return Definition.createWithoutId(
                apiDef.definition(),
                pos,
                "dictionaryapi.dev",
                apiDef.example()
        );
    }
}
