package wordwizard.repository.externaldictionaries.dto;

import java.util.List;

public record DictionaryApiResponse(
        String word,
        List<Meaning> meanings
) {}
