package wordwizard.repository.externalsourcefetching.dto;

import java.util.List;

public record Meaning(
        String partOfSpeech,
        List<ApiDefinition> definitions,
        List<String> synonyms,
        List<String> antonyms
) {}