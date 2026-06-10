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
            Long id,
            String text,
            String partOfSpeech,
            String source,
            String example
    ) {
        return new Definition(id, text, partOfSpeech, source, example, null);
    }

    public static Definition createWithoutId(
            String text,
            String partOfSpeech,
            String source,
            String example
    ) {
        return new Definition(null, text, partOfSpeech, source, example, null);
    }

    public static Definition createWithOnlyText(
            String text,
            String source
    ) {
        return new Definition(null, text, null, source, null, null);
    }
}
