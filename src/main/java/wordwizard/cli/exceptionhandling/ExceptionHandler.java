package wordwizard.cli.exceptionhandling;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExceptionHandler {
    private static final
    Map<Class<? extends Exception>,
            Function<? extends Exception, String>
            > EXCEPTION_MAP = new HashMap<>();

    static {

    }
}
