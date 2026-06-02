package wordwizard.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public Client geminiClient() {
        // Thinking budget for Flash models
        ThinkingConfig thinkingConfig = ThinkingConfig.builder()
                .thinkingBudget(512)
                .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                .maxOutputTokens(2048)
                .thinkingConfig(thinkingConfig)
                .build();

        return Client.builder()
                .apiKey(apiKey)
                .generateContentConfig(config)
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
}