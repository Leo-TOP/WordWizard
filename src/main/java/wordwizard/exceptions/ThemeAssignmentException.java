package wordwizard.exceptions;

public class ThemeAssignmentException extends RuntimeException {
    public ThemeAssignmentException(String message) {
        super(message);
    }

    public ThemeAssignmentException(String message, Throwable e) {
        super(message, e);
    }
}
