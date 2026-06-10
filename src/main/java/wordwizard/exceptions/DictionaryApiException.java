package wordwizard.exceptions;

public class DictionaryApiException extends RuntimeException {
    public DictionaryApiException(String message) {
        super(message);
    }

    public DictionaryApiException(String message, Throwable e) {
        super(message, e);
    }
}
