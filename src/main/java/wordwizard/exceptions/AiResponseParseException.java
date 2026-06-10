package wordwizard.exceptions;

public class AiResponseParseException extends GeminiApiException {
    public AiResponseParseException(String message) {
        super(message);
    }

    public AiResponseParseException(String message, Throwable e) {
        super(message, e);
    }
}
