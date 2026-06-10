package wordwizard.repository.externaldictionaries;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import wordwizard.exceptions.DictionaryApiException;
import wordwizard.repository.externaldictionaries.dto.DictionaryApiResponse;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DictionaryApiClient {
        private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        private static final Type LIST_TYPE = new TypeToken<List<DictionaryApiResponse>>(){}.getType();

        private final RestTemplate restTemplate;
        private final Gson gson;

        public DictionaryApiResponse fetchWord(String word) {
                String raw = fetchRawJson(word);
                if (raw == null) return null;

                try {
                        List<DictionaryApiResponse> list = gson.fromJson(raw, LIST_TYPE);
                        if (list == null || list.isEmpty()) {
                                throw new DictionaryApiException(
                                        "Dictionary API returned an empty response for \"" + word + "\"");
                        }
                        return list.getFirst();
                } catch (JsonSyntaxException e) {
                        throw new DictionaryApiException(
                                "Dictionary API returned malformed JSON for \"" + word + "\"", e);
                }
        }


        private String fetchRawJson(String word) {
            String url = API_URL + word;
            try {
                log.debug("Fetching: {}", url);
                return restTemplate.getForObject(url, String.class);
            } catch (HttpClientErrorException.NotFound e) {
                return null;
            } catch (RestClientException e) {
                log.error("Failed to fetch '{}': {}", word, e.getMessage());
                throw new DictionaryApiException(
                        "Dictionary API request failed for \"" + word + "\": " + e.getMessage(), e);
            }
        }
}
