package de.vocabulary.domain;

/** Form that is valid only together with a matching context. */
public sealed interface Form permits NounForm, VerbForm, AdjectiveForm {}
