package wordwizard.service.save;

public record UserWordRequest(String word,
                              String definition,
                              String pos) {}
