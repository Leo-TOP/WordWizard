package wordwizard.businesslogic.outputprocessing;

import java.util.List;

public class WordProcessor implements OutputProcessor<List<String>> {
    private final String word;
    private final String[] processingArray;

    public WordProcessor(String word) {
        this.word = word.strip().toLowerCase();
        this.processingArray = this.word.split(" ");
    }

    @Override
    public List<String> process() {
        validate();
        return getPair();
    }

    private boolean validate(){
       int processingArrayLength = processingArray.length;
       return (processingArrayLength <= 2 && processingArrayLength > 0) && containsOnlyLatin();
    }

    private boolean containsOnlyLatin(){
        return word.matches("^[A-Za-z\\s]+$");
    }

    private String getWordPartOfSpeech(){
        String articleOrPreposition = processingArray[0];

        return switch (articleOrPreposition) {
            case "a", "the", "an" -> "noun";
            case "to" -> "verb";
            default -> "";
        };
    }

    private List<String> getPair(){
        return List.of(word, getWordPartOfSpeech());
    }
}
