package wordwizard.repository.externalsourcefetching.dto;

import java.util.List;

public record ApiDefinition(
        String definition,
        String example,
        List<String> synonyms,
        List<String> antonyms
) {}