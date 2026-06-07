package wordwizard.service.validation;

import wordwizard.exceptions.InvalidWordRequestException;

import java.util.List;


public final class WordsValidator {
    private static final String WORD_PATTERN =  "^[A-Za-z\\s-]+$";

    public static void validateWords(List<String> words) {
        words.forEach(WordsValidator::validateWord);
    }

    public static void validateWord(String word) {
        if (word == null || word.isBlank()) {
            throw new InvalidWordRequestException("Word must not be blank");
        }

        if (!word.matches(WORD_PATTERN)) {
             throw new InvalidWordRequestException("Invalid word: \"" + word + "\"");
        }
    }
}
