package wordwizard.service.save.dto;

public record UserWordRequest(String word,
                              String definition,
                              String partOfSpeech) {}
