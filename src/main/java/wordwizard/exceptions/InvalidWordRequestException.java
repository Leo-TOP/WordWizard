package wordwizard.exceptions;

public class InvalidWordRequestException extends RuntimeException {
    public InvalidWordRequestException(String message) {
        super(message);
    }
}
