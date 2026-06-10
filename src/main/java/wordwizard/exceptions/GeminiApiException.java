package wordwizard.exceptions;

public class GeminiApiException extends RuntimeException {
    public GeminiApiException(String message) {
        super(message);
    }

    public GeminiApiException(String message, Throwable e) {
        super(message, e);
    }
}
