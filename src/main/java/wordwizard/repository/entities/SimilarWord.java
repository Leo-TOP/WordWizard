package wordwizard.repository.entities;

import lombok.Builder;

@Builder(toBuilder = true)
public record SimilarWord(
        String word,
        String definition,
        double distance
) {}
