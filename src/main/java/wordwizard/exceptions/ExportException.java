package wordwizard.exceptions;

public class ExportException extends RuntimeException {
    public ExportException(String message) {
        super(message);
    }

    public ExportException(String message, Throwable e) {
        super(message, e);
    }
}
