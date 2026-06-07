package wordwizard.exceptions;

public class PermissionDeniedException extends FileException {
    public PermissionDeniedException(String message) {
        super(message);
    }
    public PermissionDeniedException(String message, Throwable e) {
        super(message, e);
    }
}
