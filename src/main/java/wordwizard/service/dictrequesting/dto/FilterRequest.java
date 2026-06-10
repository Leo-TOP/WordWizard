package wordwizard.service.dictrequesting.dto;

public record FilterRequest(String theme,
                            String partOfSpeech,
                            String startsWith) {}
