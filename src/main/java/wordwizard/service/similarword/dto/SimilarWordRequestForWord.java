package wordwizard.service.similarword.dto;

import javax.validation.constraints.Min;

public record SimilarWordRequestForWord(String word,
                                        @Min(1) Integer limit,
                                        String partOfSpeech) {}
