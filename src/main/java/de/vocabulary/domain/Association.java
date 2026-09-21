package de.vocabulary.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * N:M association between exactly one German and one English vocable of the
 * same context and form.
 */
public class Association {

  private final String id;
  private final Vocable german;
  private final Vocable english;

  Association(Vocable german, Vocable english) {
    this(UUID.randomUUID().toString(), german, english, true);
  }

  public static Association of(String id, Vocable german, Vocable english) {
    return new Association(id, german, english, true);
  }

  private Association(String id, Vocable german, Vocable english, boolean attach) {
    if (german.getLanguage() != Language.GERMAN) {
      throw new IllegalArgumentException("The German side must have language GERMAN");
    }
    if (english.getLanguage() != Language.ENGLISH) {
      throw new IllegalArgumentException("The English side must have language ENGLISH");
    }
    if (german.getContext() != english.getContext()) {
      throw new IllegalArgumentException("Associated vocables must share the same context");
    }
    if (!german.getForm().equals(english.getForm())) {
      throw new IllegalArgumentException("Associated vocables must share the same form");
    }
    this.id = Objects.requireNonNull(id, "id");
    this.german = german;
    this.english = english;
    if (attach) {
      german.addAssociationAsGerman(this);
      english.addAssociationAsEnglish(this);
    }
  }

  void detach() {
    german.removeAssociationAsGerman(this);
    english.removeAssociationAsEnglish(this);
  }

  public String getId() {
    return id;
  }

  public Vocable getGerman() {
    return german;
  }

  public Vocable getEnglish() {
    return english;
  }

  boolean involves(Vocable vocable) {
    return german.equals(vocable) || english.equals(vocable);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Association that)) {
      return false;
    }
    return Objects.equals(german, that.german) && Objects.equals(english, that.english);
  }

  @Override
  public int hashCode() {
    return Objects.hash(german, english);
  }
}
