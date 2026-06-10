package wordwizard.infrastructure;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import wordwizard.exceptions.GeminiApiException;

@Slf4j
@Repository
public class GeminiChatClient {
   private final Client client;
   private final String modelName;
   private final GenerateContentConfig generationConfig;

    public GeminiChatClient(Client client,
                            GenerateContentConfig generationConfig,
                            @Value("${spring.ai.google.genai.chat.options.model}") String modelName) {
        this.client = client;
        this.generationConfig = generationConfig;
        this.modelName = modelName;
    }

    public String generateContent(String prompt) {
        GenerateContentResponse response;
        try {
            response = client.models.generateContent(modelName, prompt, generationConfig);
        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            throw new GeminiApiException("Gemini API call failed: " + e.getMessage(), e);
        }

        String text = response.text();
        if (text == null || text.isBlank()) {
            throw new GeminiApiException(
                    "Gemini returned an empty response (model: " + modelName
                            + ") — the answer may have been blocked or truncated");
        }
        return text;
    }
}
