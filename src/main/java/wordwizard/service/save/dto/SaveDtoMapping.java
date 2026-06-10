package wordwizard.service.save.dto;

import org.springframework.stereotype.Component;
import wordwizard.models.Definition;
import wordwizard.models.Word;

import java.util.List;

@Component
public class SaveDtoMapping {

    public Word mapToWord(UserWordRequest request){
        List<Definition> definitions = List.of(mapToDefinition(
                request.definition())
        );

        return Word.createWordWithoutId(request.word(), definitions);
    }

    public Definition mapToDefinition(String definition) {
        return Definition.createWithOnlyText(
                definition,
                "user");
    }
}
