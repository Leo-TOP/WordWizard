package wordwizard.service.wordsfetching.dto;

public record WordRequest(
        String word,
        Integer limit,
        String partOfSpeech){}
