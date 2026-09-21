package de.vocabulary.domain;

import java.util.Objects;

/** Vocable in the noun context. */
public class Noun extends Vocable {

  private final NounForm form;

  Noun(String text, Language language, NounForm form) {
    super(text, language);
    this.form = Objects.requireNonNull(form, "form");
  }

  Noun(String id, String text, Language language, NounForm form) {
    super(id, text, language);
    this.form = Objects.requireNonNull(form, "form");
  }

  @Override
  public Context getContext() {
    return Context.NOUN;
  }

  @Override
  public NounForm getForm() {
    return form;
  }
}
