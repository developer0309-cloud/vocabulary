package de.vocabulary.service;

import de.vocabulary.domain.Vocable;

/** Result of scoring a translation against a vocable's associates. */
public record TranslationScore(boolean correct, Vocable vocable) {}
