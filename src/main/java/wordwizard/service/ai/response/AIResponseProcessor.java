package wordwizard.service.ai.response;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wordwizard.service.ai.response.dto.BatchThemeResponse;
import wordwizard.service.ai.response.dto.SingleThemeResponse;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIResponseProcessor {
    private final Gson gson;

    public BatchThemeResponse parseBatchResponse(String rawResponse) {
        try {
            return gson.fromJson(rawResponse.trim(), BatchThemeResponse.class);
        } catch (JsonSyntaxException e) {
            log.error("Failed to parse AI response: {}", rawResponse, e);
            return new BatchThemeResponse(new ArrayList<>());
        }
    }

    public SingleThemeResponse parseSingleResponse(String rawResponse) {
        try {
            return gson.fromJson(rawResponse.trim(), SingleThemeResponse.class);
        } catch (JsonSyntaxException e) {
            log.error("Failed to parse AI response: {}", rawResponse, e);
            return new SingleThemeResponse("unclassified");
        }
    }
}