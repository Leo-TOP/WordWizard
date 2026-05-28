package wordwizard.repository.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiThemeRetriever {

    public String getWordTheme() {
        Client client = new Client();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash",
                        "Explain how AI works in a few words",
                        null);

        return response.text();
    }
}
