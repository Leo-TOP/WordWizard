package wordwizard.repository.externaldictionaries;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import wordwizard.repository.externaldictionaries.dto.DictionaryApiResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DictionaryApiClient {
        private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        private final RestTemplate restTemplate;
        private final Gson gson;

        public DictionaryApiResponse fetchWord(String word){
            return gson.fromJson(fetchRawJson(word), DictionaryApiResponse.class);
        }

        private String fetchRawJson(String word) {
            try {
                String url = API_URL + word;
                log.debug("Fetching: {}", url);
                return restTemplate.getForObject(url, String.class);
            } catch (Exception e) {
                log.error("Failed to fetch '{}': {}", word, e.getMessage());
                return null;
            }
        }
}

