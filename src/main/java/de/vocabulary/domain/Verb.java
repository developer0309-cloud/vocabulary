package de.vocabulary.domain;

import java.util.Objects;

/** Vocable in the verb context. */
public class Verb extends Vocable {

  private final VerbForm form;

  Verb(String text, Language language, VerbForm form) {
    super(text, language);
    this.form = Objects.requireNonNull(form, "form");
  }

  Verb(String id, String text, Language language, VerbForm form) {
    super(id, text, language);
    this.form = Objects.requireNonNull(form, "form");
  }

  @Override
  public Context getContext() {
    return Context.VERB;
  }

  @Override
  public VerbForm getForm() {
    return form;
  }
}
