package wordwizard.service.wordsfetching.dto;

import javax.validation.constraints.Min;

public record WordRequest(
        String word,
        @Min(1) Integer limit,
        String partOfSpeech
){}
