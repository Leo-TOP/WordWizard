package wordwizard.repository.externalsourcefetching.dto;

import java.util.List;

public record DictionaryApiResponse(
        String word,
        List<Meaning> meanings
) {}
