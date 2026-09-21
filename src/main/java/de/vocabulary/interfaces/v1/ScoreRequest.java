package de.vocabulary.interfaces.v1;

import jakarta.validation.constraints.NotBlank;

public record ScoreRequest(@NotBlank String translation) {}
