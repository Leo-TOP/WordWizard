package wordwizard.service.ai.response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.exceptions.AiResponseParseException;
import wordwizard.service.ai.response.dto.BatchThemeResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIResponseProcessor {
    private static final int SNIPPET_LENGTH = 200;

    private final Gson gson;

    public BatchThemeResponse parseBatchResponse(String rawResponse) {
        try {
            BatchThemeResponse parsed = gson.fromJson(rawResponse.trim(), BatchThemeResponse.class);
            if (parsed == null) {
                throw new AiResponseParseException(
                        "AI response was blank — expected JSON with \"theme_assignments\"");
            }
            return parsed;
        } catch (JsonSyntaxException e) {
            log.error("Failed to parse AI response: {}", rawResponse, e);
            throw new AiResponseParseException(
                    "AI returned invalid JSON: " + snippet(rawResponse), e);
        }
    }

    private String snippet(String raw) {
        String trimmed = raw.trim();
        return trimmed.length() <= SNIPPET_LENGTH
                ? trimmed
                : trimmed.substring(0, SNIPPET_LENGTH) + "…";
    }
}
