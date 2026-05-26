package wordwizard.exceptions;
import lombok.*;

@Getter
@AllArgsConstructor
public class WordNotFoundException extends RuntimeException {
    private final String word;
}
