package wordwizard.repository.entities;

/**
 * Immutable domain entity for a single word definition.
 *
 * {@code embedding} is {@code null} until computed by {@link wordwizard.businesslogic.embeddingapplication.EmbeddingService}
 * and persisted. All other fields are non-null after construction via the factory method.
 */
public record Definition(
        Long   id,
        String text,
        String partOfSpeech,
        String example,
        float[] embedding
) {

    public static Definition create(
            String text,
            String partOfSpeech,
            String example
    ) {
        return new Definition(null, text, partOfSpeech, example, null);
    }

    public Definition withEmbedding(float[] embedding) {
        return new Definition(id, text, partOfSpeech, example, embedding);
    }
}
