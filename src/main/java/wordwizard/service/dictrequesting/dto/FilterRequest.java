package wordwizard.service.dictrequesting.dto;

public record FilterRequest(String theme, String partOfSpeech, String startsWith) {
    public boolean isEmpty(){
        return theme == null &&
                partOfSpeech == null &&
                startsWith == null;
    }
}
