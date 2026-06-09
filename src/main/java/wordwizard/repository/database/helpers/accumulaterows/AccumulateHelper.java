package wordwizard.repository.database.helpers.accumulaterows;

import wordwizard.models.Definition;
import wordwizard.models.Word;
import wordwizard.repository.database.helpers.CastHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class AccumulateHelper {
        private final Long          id;
        private final String        word;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;
        private final List<Definition> definitions = new ArrayList<>();

        public AccumulateHelper(Map<String, Object> row) {
            this.id            = CastHelper.toLong(row.get("id"));
            this.word          = (String) row.get("word");
            this.createdAt     = CastHelper.toLocalDateTime(row.get("created_at"));
            this.updatedAt     = CastHelper.toLocalDateTime(row.get("updated_at"));
        }

        public void addDefinition(Definition definition) {
            definitions.add(definition);
        }

        public Word build() {
            return new Word(id, word, createdAt, updatedAt, definitions);
        }
}
