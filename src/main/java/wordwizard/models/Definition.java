package wordwizard.models;

public record Definition(
        Long   id,
        String text,
        String partOfSpeech,
        String source,
        String example,
        float[] embedding
) {

    public static Definition create(
            String text,
            String partOfSpeech,
            String source,
            String example
    ) {
        return new Definition(null, text, partOfSpeech, source, example, null);
    }
}
