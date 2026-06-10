package wordwizard.config;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.ThinkingConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;


    @Bean
    public GenerateContentConfig geminiGenerationConfig() {
        return GenerateContentConfig.builder()
                .maxOutputTokens(65536)
                .responseMimeType("application/json")
                .thinkingConfig(ThinkingConfig.builder().thinkingBudget(512).build())
                .build();
    }

    @Bean
    public Client geminiClient() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Gemini API key is empty — set GOOGLE_GENAI_API_KEY in .env");
        }
        return Client.builder()
                .apiKey(apiKey)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService wordImportExecutor(
            @Value("${word.import.pool-size:5}") int poolSize) {
        return Executors.newFixedThreadPool(poolSize, r -> {
            Thread t = new Thread(r);
            t.setName("word-import-" + t.threadId());
            t.setDaemon(true);
            return t;
        });
    }
}