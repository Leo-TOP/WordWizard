package wordwizard.exceptions;

public class InvalidStartsWithException extends RuntimeException {
    public InvalidStartsWithException(String message) {
        super(message);
    }
}
