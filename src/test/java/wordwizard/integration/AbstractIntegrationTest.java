package wordwizard.integration;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import wordwizard.cli.CliEngine;
import wordwizard.infrastructure.GeminiChatClient;

import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Shared Spring context for all integration tests:
 * - real PostgreSQL with pgvector (Testcontainers, singleton container, real initDB/init.sql schema)
 * - mocked GeminiChatClient (no real AI calls)
 * - mocked EmbeddingModel with deterministic vectors (no ONNX loading)
 * - MockRestServiceServer bound to the real RestTemplate (covers DictionaryApiClient code)
 * - mocked CliEngine so the interactive loop never starts
 */
@SpringBootTest
public abstract class AbstractIntegrationTest {

    protected static final String DICTIONARY_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16")
                    .asCompatibleSubstituteFor("postgres"))
                    .withCopyFileToContainer(
                            MountableFile.forHostPath(
                                    Paths.get("initDB", "init.sql").toAbsolutePath().toString()),
                            "/docker-entrypoint-initdb.d/init.sql");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.ai.google.genai.api-key", () -> "test-api-key");
    }

    @MockBean
    protected CliEngine cliEngine;

    @MockBean
    protected EmbeddingModel embeddingModel;

    @MockBean
    protected GeminiChatClient geminiChatClient;

    @Autowired
    protected JdbcTemplate jdbc;

    @Autowired
    protected RestTemplate restTemplate;

    protected MockRestServiceServer dictionaryServer;

    /** Per-test overrides: text -> vector. Falls back to TestVectors.vectorFor. */
    protected final Map<String, float[]> customVectors = new ConcurrentHashMap<>();

    @BeforeEach
    void resetSharedState() {
        jdbc.execute("TRUNCATE TABLE word_themes, definitions, themes, words RESTART IDENTITY CASCADE");
        customVectors.clear();

        Mockito.when(embeddingModel.embed(Mockito.anyString())).thenAnswer(invocation -> {
            String text = invocation.getArgument(0, String.class);
            return customVectors.getOrDefault(text, TestVectors.vectorFor(text));
        });
        Mockito.when(geminiChatClient.generateContent(Mockito.anyString()))
                .thenReturn("{\"theme_assignments\": []}");

        dictionaryServer = MockRestServiceServer.bindTo(restTemplate)
                .ignoreExpectOrder(true)
                .build();
    }

    // ---------- helpers ----------

    /** Dictionary API payload with two definitions: one noun (with example) and one verb. */
    protected static String dictionaryJson(String word) {
        return """
            [{"word":"%s","phonetic":"/x/","meanings":[
              {"partOfSpeech":"noun","definitions":[
                  {"definition":"%s as a noun","example":"an example with %s"}]},
              {"partOfSpeech":"verb","definitions":[
                  {"definition":"%s as a verb","example":null}]}
            ]}]""".formatted(word, word, word, word);
    }

    protected static String themeJson(String word, int defIndex, String theme) {
        return "{\"theme_assignments\":[{\"word\":\"%s\",\"definition_index\":%d,\"theme\":\"%s\"}]}"
                .formatted(word, defIndex, theme);
    }

    protected void expectDictionarySuccess(String word) {
        dictionaryServer.expect(requestTo(DICTIONARY_URL + word))
                .andRespond(withSuccess(dictionaryJson(word), MediaType.APPLICATION_JSON));
    }

    protected void expectDictionaryStatus(String word, HttpStatus status) {
        dictionaryServer.expect(requestTo(DICTIONARY_URL + word))
                .andRespond(withStatus(status)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"title\":\"error\"}"));
    }

    protected void expectDictionaryBody(String word, String body) {
        dictionaryServer.expect(requestTo(DICTIONARY_URL + word))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));
    }

    protected int countRows(String table) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
        return count != null ? count : 0;
    }
}
