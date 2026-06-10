package wordwizard.cli.parsing;

import org.springframework.stereotype.Component;
import wordwizard.cli.parsing.dto.ParsedInput;
import wordwizard.exceptions.CommandValidationException;

import java.util.*;

@Component
public class InputParser {

    public ParsedInput parse(String raw) {
        List<String> tokens = tokenize(raw.trim().toLowerCase());
        if (tokens.isEmpty()) throw new CommandValidationException("Input must not be empty");

        String commandName = tokens.getFirst();
        List<String> positional = new ArrayList<>();
        Map<String, String> options = new LinkedHashMap<>();

        for (int i = 1; i < tokens.size(); i++) {
            i = parseToken(tokens, i, options, positional);
        }

        return new ParsedInput(commandName, Collections.unmodifiableList(positional),
                Collections.unmodifiableMap(options));
    }

    private List<String> tokenize(String raw) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (char c : raw.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (!current.isEmpty()) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) tokens.add(current.toString());
        return tokens;
    }

    private int parseToken(List<String> tokens, int i,
                           Map<String, String> options, List<String> positional) {
        String token = tokens.get(i);
        if (token.startsWith("--")) {
            String key = token.substring(2);
            if (i + 1 < tokens.size() && !tokens.get(i + 1).startsWith("--")) {
                options.put(key, tokens.get(i + 1));
                return i + 1;
            }
            options.put(key, "true");
        } else {
            positional.add(token);
        }
        return i;
    }
}
