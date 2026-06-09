package wordwizard.models;

import java.util.Map;

public record VocabularyStats(
        int totalWords,
        int totalDefinitions,
        double avgDefinitionsPerWord,
        Map<String, Integer> wordsByPos,
        Map<String, Integer> wordsByTheme,
        int totalThemes,
        int wordsAddedLast7Days,
        int wordsAddedLast30Days
) {}