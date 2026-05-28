package wordwizard.businesslogic;

import org.springframework.stereotype.Service;
import wordwizard.businesslogic.embeddingapplication.EmbeddingService;

@Service
public class WordWizardService {
    private final EmbeddingService embeddingService;

    public WordWizardService(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    String getInfo(){return null;}
    void saveInfo() {}
}
