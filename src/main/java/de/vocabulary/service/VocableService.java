package de.vocabulary.service;

import de.vocabulary.domain.Context;
import de.vocabulary.domain.Language;
import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.VocableRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class VocableService {

  private final VocableRepository vocables;

  @Inject
  public VocableService(VocableRepository vocables) {
    this.vocables = vocables;
  }

  public Optional<Vocable> findRandom(Language language, Context context) {
    Objects.requireNonNull(language, "language");
    Objects.requireNonNull(context, "context");
    return vocables.findRandom(language, context);
  }

  @Transactional
  public Optional<TranslationScore> score(String vocableId, String translation) {
    Objects.requireNonNull(vocableId, "vocableId");
    Objects.requireNonNull(translation, "translation");
    return vocables
        .findById(vocableId)
        .map(vocable -> new TranslationScore(vocable.acceptsTranslation(translation), vocable));
  }
}
