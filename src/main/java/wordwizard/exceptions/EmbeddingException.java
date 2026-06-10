package wordwizard.exceptions;

public class EmbeddingException extends RuntimeException {
    public EmbeddingException(String message) {
        super(message);
    }

    public EmbeddingException(String message, Throwable e) {
        super(message, e);
    }
}
