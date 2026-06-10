package wordwizard.exceptions;

public class DataMappingException extends RuntimeException {
    public DataMappingException(String message) {
        super(message);
    }

    public DataMappingException(String message, Throwable e) {
        super(message, e);
    }
}
