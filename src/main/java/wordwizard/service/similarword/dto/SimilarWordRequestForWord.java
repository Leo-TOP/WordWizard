package wordwizard.service.similarword.dto;

public record SimilarWordRequestForWord(String word,
                                        Integer limit,
                                        String partOfSpeech) {}
