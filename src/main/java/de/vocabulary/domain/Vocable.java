package de.vocabulary.domain;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Aggregate root. A vocable has text, language, exactly one context and exactly
 * one matching form. Subtypes are disjoint. Every vocable is associated with at
 * least one vocable of the other language.
 */
public abstract class Vocable {

  private static final int TEXT_MAX_LENGTH = 255;

  private final String id;
  private final String text;
  private final String textNormalized;
  private final Language language;
  private Association requiredAssociation;
  private final Set<Association> associationsAsGerman = new HashSet<>();
  private final Set<Association> associationsAsEnglish = new HashSet<>();
  private boolean markedForDeletion;

  protected Vocable(String text, Language language) {
    this(UUID.randomUUID().toString(), text, language);
  }

  protected Vocable(String id, String text, Language language) {
    requireValidText(text);
    this.id = Objects.requireNonNull(id, "id");
    this.text = text;
    this.textNormalized = text.toLowerCase(Locale.ROOT);
    this.language = Objects.requireNonNull(language, "language");
  }

  public static Noun noun(String text, Language language, NounForm form) {
    return new Noun(text, language, form);
  }

  public static Verb verb(String text, Language language, VerbForm form) {
    return new Verb(text, language, form);
  }

  public static Adjective adjective(String text, Language language, AdjectiveForm form) {
    return new Adjective(text, language, form);
  }

  public static Noun noun(String id, String text, Language language, NounForm form) {
    return new Noun(id, text, language, form);
  }

  public static Verb verb(String id, String text, Language language, VerbForm form) {
    return new Verb(id, text, language, form);
  }

  public static Adjective adjective(
      String id, String text, Language language, AdjectiveForm form) {
    return new Adjective(id, text, language, form);
  }

  /**
   * Associates this vocable with one of the other language. Both must share
   * context and form. German and English may each have several partners.
   */
  public void associateWith(Vocable other) {
    Objects.requireNonNull(other, "other");
    Vocable german;
    Vocable english;
    if (language == Language.GERMAN && other.language == Language.ENGLISH) {
      german = this;
      english = other;
    } else if (language == Language.ENGLISH && other.language == Language.GERMAN) {
      german = other;
      english = this;
    } else {
      throw new IllegalArgumentException(
          "Association is only allowed between a German and an English vocable");
    }
    boolean alreadyPresent =
        german.associationsAsGerman.stream().anyMatch(a -> a.getEnglish().equals(english));
    if (!alreadyPresent) {
      new Association(german, english);
    }
  }

  /**
   * Case-insensitive match of {@code translation} against any associate text.
   */
  public boolean acceptsTranslation(String translation) {
    if (translation == null || translation.isBlank()) {
      return false;
    }
    String answer = translation.trim();
    return getAssociates().stream()
        .anyMatch(associate -> associate.getText().equalsIgnoreCase(answer));
  }

  /** Partners in the other language. */
  public Set<Vocable> getAssociates() {
    if (language == Language.GERMAN) {
      return associationsAsGerman.stream()
          .map(Association::getEnglish)
          .collect(Collectors.toUnmodifiableSet());
    }
    return associationsAsEnglish.stream()
        .map(Association::getGerman)
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Domain deletion: associations of this vocable are dropped. Partners left
   * without an association are deleted as well. Return value is every vocable
   * that must be removed from persistence (including this one).
   */
  public Set<Vocable> remove() {
    Set<Vocable> toDelete = deletionChain();
    toDelete.forEach(v -> v.markedForDeletion = true);
    return Set.copyOf(toDelete);
  }

  public void assertPersistable() {
    if (markedForDeletion) {
      return;
    }
    if (requiredAssociation == null || getAssociates().isEmpty()) {
      throw new IllegalStateException(
          "A vocable must be associated with at least one vocable of the other language");
    }
    if (!requiredAssociation.involves(this)) {
      throw new IllegalStateException("required_association_id must include this vocable");
    }
  }

  public boolean isMarkedForDeletion() {
    return markedForDeletion;
  }

  private Set<Vocable> deletionChain() {
    Map<Vocable, Set<Vocable>> remaining = new HashMap<>();
    Set<Vocable> toDelete = new LinkedHashSet<>();
    Queue<Vocable> queue = new ArrayDeque<>();
    toDelete.add(this);
    queue.add(this);
    remaining.put(this, new HashSet<>(getAssociates()));
    while (!queue.isEmpty()) {
      Vocable current = queue.remove();
      for (Vocable partner : new HashSet<>(remaining.get(current))) {
        remaining.computeIfAbsent(partner, v -> new HashSet<>(v.getAssociates()));
        remaining.get(current).remove(partner);
        remaining.get(partner).remove(current);
        if (remaining.get(partner).isEmpty() && toDelete.add(partner)) {
          queue.add(partner);
        }
      }
    }
    return toDelete;
  }

  void addAssociationAsGerman(Association association) {
    associationsAsGerman.add(association);
    if (requiredAssociation == null) {
      requiredAssociation = association;
    }
  }

  void addAssociationAsEnglish(Association association) {
    associationsAsEnglish.add(association);
    if (requiredAssociation == null) {
      requiredAssociation = association;
    }
  }

  void removeAssociationAsGerman(Association association) {
    associationsAsGerman.remove(association);
    if (association.equals(requiredAssociation)) {
      requiredAssociation = nextRequiredAssociation();
    }
  }

  void removeAssociationAsEnglish(Association association) {
    associationsAsEnglish.remove(association);
    if (association.equals(requiredAssociation)) {
      requiredAssociation = nextRequiredAssociation();
    }
  }

  private Association nextRequiredAssociation() {
    if (!associationsAsGerman.isEmpty()) {
      return associationsAsGerman.iterator().next();
    }
    if (!associationsAsEnglish.isEmpty()) {
      return associationsAsEnglish.iterator().next();
    }
    return null;
  }

  private static void requireValidText(String text) {
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException("Text must not be blank");
    }
    if (text.length() > TEXT_MAX_LENGTH) {
      throw new IllegalArgumentException("Text must be at most " + TEXT_MAX_LENGTH + " characters");
    }
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public String getTextNormalized() {
    return textNormalized;
  }

  public Language getLanguage() {
    return language;
  }

  public Association getRequiredAssociation() {
    return requiredAssociation;
  }

  public Set<Association> getAssociationsAsGerman() {
    return Set.copyOf(associationsAsGerman);
  }

  public Set<Association> getAssociationsAsEnglish() {
    return Set.copyOf(associationsAsEnglish);
  }

  public abstract Context getContext();

  public abstract Form getForm();

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Vocable that)) {
      return false;
    }
    return language == that.language
        && getContext() == that.getContext()
        && text.equalsIgnoreCase(that.text)
        && Objects.equals(getForm(), that.getForm());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        text == null ? null : text.toLowerCase(Locale.ROOT),
        language,
        getContext(),
        getForm());
  }
}
