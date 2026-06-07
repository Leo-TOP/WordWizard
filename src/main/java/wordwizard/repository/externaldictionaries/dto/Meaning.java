package wordwizard.repository.externaldictionaries.dto;

import java.util.List;

public record Meaning(
        String partOfSpeech,
        List<ApiDefinition> definitions
) {}