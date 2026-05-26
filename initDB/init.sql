CREATE TABLE words (
                       id SERIAL PRIMARY KEY,
                       word VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_user_defined BOOLEAN DEFAULT FALSE
);

CREATE TABLE definitions (
                             id SERIAL PRIMARY KEY,
                             word_id INTEGER REFERENCES words(id),
                             definition TEXT NOT NULL,
                             part_of_speech VARCHAR(50),
                             source VARCHAR(255),
                             embedding vector(384)
);

CREATE TABLE themes (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        description TEXT
);

CREATE TABLE word_themes (
                             word_id INTEGER REFERENCES words(id),
                             theme_id INTEGER REFERENCES themes(id),
                             definition_id INTEGER REFERENCES definitions(id),
                             PRIMARY KEY (word_id, theme_id, definition_id)
);