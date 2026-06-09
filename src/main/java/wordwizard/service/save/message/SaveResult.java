package wordwizard.service.save.message;

public record SaveResult(
        String word,
        SaveStatus status,
        String message
) {}
