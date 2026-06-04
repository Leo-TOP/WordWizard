package wordwizard.repository.database.repos;

import com.pgvector.PGvector;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wordwizard.repository.entities.Definition;
import wordwizard.repository.entities.SimilarWord;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcDefinitionRepository implements DefinitionRepository {
    private final JdbcTemplate jdbc;

    private static final RowMapper<Definition> DEFINITION_MAPPER = (rs, rowNum) -> new Definition(
            rs.getLong("id"),
            rs.getString("definition"),
            rs.getString("part_of_speech"),
            rs.getString("example"),
            null
    );

    private static final RowMapper<SimilarWord> SIMILAR_WORD_MAPPER = (rs, rowNum) -> new SimilarWord(
            rs.getString("word"),
            rs.getString("definition"),
            rs.getDouble("distance")
    );


    @Override
    public Long save(Long wordId, Definition definition) {
        return jdbc.queryForObject(
                """
                INSERT INTO definitions (word_id, definition, part_of_speech, example)
                VALUES (?, ?, ?, ?) RETURNING id
                """,
                Long.class,
                wordId,
                definition.text(),
                definition.partOfSpeech(),
                definition.example()
        );
    }

    @Override
    public void updateEmbedding(Long definitionId, float[] embedding) {
        jdbc.update(
                "UPDATE definitions SET embedding = ? WHERE id = ?",
                new PGvector(embedding),
                definitionId
        );
    }


    @Override
    public List<Definition> findByWordId(Long wordId) {
        return jdbc.query(
                """
                SELECT id, definition, part_of_speech, source, example
                FROM definitions
                WHERE word_id = ?
                ORDER BY id
                """,
                DEFINITION_MAPPER,
                wordId
        );
    }


    @Override
    public List<SimilarWord> findByEmbedding(float[] queryVector, int limit) {
        return jdbc.query(
                """
                SELECT w.word, d.definition, d.embedding <=> ? AS distance
                FROM definitions d
                JOIN words w ON d.word_id = w.id
                WHERE d.embedding IS NOT NULL
                ORDER BY distance
                LIMIT ?
                """,
                SIMILAR_WORD_MAPPER,
                new PGvector(queryVector),
                limit
        );
    }

    @Override
    public List<SimilarWord> findSimilarTo(String excludeWord, float[] queryVector, int limit) {
        return jdbc.query(
                """
                SELECT w.word, d.definition, d.embedding <=> ? AS distance
                FROM definitions d
                JOIN words w ON d.word_id = w.id
                WHERE w.word != ?
                  AND d.embedding IS NOT NULL
                ORDER BY distance
                LIMIT ?
                """,
                SIMILAR_WORD_MAPPER,
                new PGvector(queryVector),
                excludeWord,
                limit
        );
    }
}
