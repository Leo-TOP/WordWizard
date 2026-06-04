package wordwizard.businesslogic.dtomapping;

import org.springframework.stereotype.Service;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.Word;
import wordwizard.repository.externalsourcefetching.dto.DictionaryApiResponse;

import java.util.List;

/**
 * Maps external API responses to domain entities.
 */
@Service
public class DtoMapper {

    /**
     * Convert a {@link DictionaryApiResponse} into an unsaved {@link Word} entity.
     *
     * <p>The returned {@link Word} and its {@link Definition}s have {@code null} IDs —
     * they are assigned by the database after insertion.
     */
    public Word mapToWord(String wordText, DictionaryApiResponse response) {
        List<Definition> definitions = response.meanings().stream()
                .flatMap(meaning -> meaning.definitions().stream()
                        .map(apiDef -> Definition.create(
                                apiDef.definition(),
                                meaning.partOfSpeech(),
                                apiDef.example()
                        ))
                )
                .toList();

        return Word.create(wordText, false, definitions);
    }
}
