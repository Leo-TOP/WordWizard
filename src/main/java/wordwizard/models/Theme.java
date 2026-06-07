package wordwizard.models;

public record Theme(
        Long   id,
        String name,
        long   wordCount
) {
    public static Theme create(String name, String description) {
        return new Theme
                (null, name ,0);
    }
}
