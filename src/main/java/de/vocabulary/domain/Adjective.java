package de.vocabulary.domain;

import java.util.Objects;

/** Vocable in the adjective context. */
public class Adjective extends Vocable {

  private final AdjectiveForm form;

  Adjective(String text, Language language, AdjectiveForm form) {
    super(text, language);
    this.form = Objects.requireNonNull(form, "form");
  }

  Adjective(String id, String text, Language language, AdjectiveForm form) {
    super(id, text, language);
    this.form = Objects.requireNonNull(form, "form");
  }

  @Override
  public Context getContext() {
    return Context.ADJECTIVE;
  }

  @Override
  public AdjectiveForm getForm() {
    return form;
  }
}
