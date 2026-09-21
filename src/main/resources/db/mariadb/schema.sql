CREATE DATABASE IF NOT EXISTS vocabulary
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE vocabulary;

-- Insert associations first, then vocables (required_association_id is NOT NULL).
-- Delete vocables first, then leftover vocable_association rows.
-- No foreign key from vocable_association to vocable.

CREATE TABLE vocable_association (
  id VARCHAR(36) NOT NULL,
  german_vocable_id VARCHAR(36) NOT NULL,
  english_vocable_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_vocable_association_de_en
    UNIQUE (german_vocable_id, english_vocable_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE vocable (
  id VARCHAR(36) NOT NULL,
  vocable_text VARCHAR(255) NOT NULL,
  vocable_text_normalized VARCHAR(255) NOT NULL,
  language VARCHAR(32) NOT NULL,
  required_association_id VARCHAR(36) NOT NULL,
  context VARCHAR(32) NOT NULL,
  form VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_vocable_disjoint
    UNIQUE (vocable_text_normalized, language, context, form),
  CONSTRAINT fk_vocable_required_association
    FOREIGN KEY (required_association_id) REFERENCES vocable_association (id),
  CONSTRAINT ck_vocable_language
    CHECK (language IN ('GERMAN', 'ENGLISH')),
  CONSTRAINT ck_vocable_context
    CHECK (context IN ('NOUN', 'VERB', 'ADJECTIVE')),
  CONSTRAINT ck_vocable_form
    CHECK (
      (context = 'NOUN' AND form IN ('SINGULAR', 'PLURAL'))
      OR (context = 'VERB' AND form IN ('PRESENT', 'FIRST_PAST', 'SECOND_PAST'))
      OR (
        context = 'ADJECTIVE'
        AND form IN ('BASE', 'FIRST_COMPARATIVE', 'SECOND_COMPARATIVE')
      )
    ),
  CONSTRAINT ck_vocable_text_not_blank
    CHECK (vocable_text <> '' AND vocable_text_normalized <> ''),
  INDEX idx_vocable_language_context (language, context)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
