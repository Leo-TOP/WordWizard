CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS words (
    id              SERIAL PRIMARY KEY,
    word            VARCHAR(255) NOT NULL UNIQUE,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS definitions (
    id             SERIAL PRIMARY KEY,
    word_id        INTEGER      NOT NULL REFERENCES words(id) ON DELETE CASCADE,
    definition     TEXT         NOT NULL,
    part_of_speech VARCHAR(50),
    example        TEXT,
    embedding      vector(384)
);

CREATE INDEX IF NOT EXISTS idx_definitions_embedding
    ON definitions USING ivfflat (embedding vector_cosine_ops)
    WITH (lists = 100);

CREATE TABLE IF NOT EXISTS themes (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS word_themes (
    word_id       INTEGER NOT NULL REFERENCES words(id)       ON DELETE CASCADE,
    theme_id      INTEGER NOT NULL REFERENCES themes(id)      ON DELETE CASCADE,
    definition_id INTEGER NOT NULL REFERENCES definitions(id) ON DELETE CASCADE,
    PRIMARY KEY (word_id, theme_id, definition_id)
);
