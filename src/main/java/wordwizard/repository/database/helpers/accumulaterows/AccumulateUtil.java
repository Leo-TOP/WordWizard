package wordwizard.repository.database.helpers.accumulaterows;

import wordwizard.exceptions.DataMappingException;
import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.helpers.CastHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AccumulateUtil {

    private AccumulateUtil() {}

    public static List<Word> accumulateRows(List<Map<String, Object>> rows) {
        Map<Long, AccumulateHelper> accumulators = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Long wordId = requireWordId(row);
            AccumulateHelper acc = accumulators.computeIfAbsent(wordId, id -> new AccumulateHelper(row));

            Long defId = CastHelper.toLong(row.get("def_id"));
            if (defId != null) {
                acc.addDefinition(new Definition(
                        defId,
                        (String) row.get("definition"),
                        (String) row.get("part_of_speech"),
                        (String) row.get("source"),
                        (String) row.get("example"),
                        null
                ));
            }
        }

        return accumulators.values().stream()
                .map(AccumulateHelper::build)
                .toList();
    }

    public static Map<String, List<Word>> accumulateGroupedRows(List<Map<String, Object>> rows) {
        Map<String, Map<Long, AccumulateHelper>> grouped = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            String themeName = (String) row.get("theme_name");
            Long wordId = requireWordId(row);

            Map<Long, AccumulateHelper> themeGroup =
                    grouped.computeIfAbsent(themeName, k -> new LinkedHashMap<>());
            AccumulateHelper acc = themeGroup.computeIfAbsent(wordId, id -> new AccumulateHelper(row));

            Long defId = CastHelper.toLong(row.get("def_id"));
            if (defId != null) {
                acc.addDefinition(new Definition(
                        defId,
                        (String) row.get("definition"),
                        (String) row.get("part_of_speech"),
                        (String) row.get("source"),
                        (String) row.get("example"),
                        null
                ));
            }
        }

        Map<String, List<Word>> result = new LinkedHashMap<>();
        grouped.forEach((themeName, accMap) ->
                result.put(themeName, accMap.values().stream().map(AccumulateHelper::build).toList()));
        return result;
    }

    /* A null id key would silently merge every word into a single accumulator. */
    private static Long requireWordId(Map<String, Object> row) {
        Long wordId = CastHelper.toLong(row.get("id"));
        if (wordId == null) {
            throw new DataMappingException(
                    "Database row for word \"" + row.get("word") + "\" has no id — cannot group definitions");
        }
        return wordId;
    }
}
