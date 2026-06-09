package wordwizard.infrastructure;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

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
        try {
            GenerateContentResponse response =
                    client.models.generateContent(modelName, prompt, generationConfig);
            return response.text();
        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            throw new RuntimeException("Gemini generation error", e);
        }
    }
}
