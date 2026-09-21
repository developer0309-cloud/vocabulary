package de.vocabulary.infra.repository.seed;

import de.vocabulary.domain.AdjectiveForm;
import de.vocabulary.domain.Association;
import de.vocabulary.domain.Language;
import de.vocabulary.domain.NounForm;
import de.vocabulary.domain.VerbForm;
import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.VocableRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Loads a German–English catalog covering every context and form. Associations
 * are inserted before vocables so {@code required_association_id} can be set.
 */
@ApplicationScoped
public class VocabularySeed {

  private final VocableRepository vocables;
  private final EntityManager entityManager;

  @Inject
  public VocabularySeed(VocableRepository vocables, EntityManager entityManager) {
    this.vocables = vocables;
    this.entityManager = entityManager;
  }

  @Transactional
  public void load() {
    if (vocables.count() > 0) {
      return;
    }
    seedNouns();
    seedVerbs();
    seedAdjectives();
  }

  private void seedNouns() {
    pairNoun("Haus", "house", NounForm.SINGULAR);
    pairNoun("Häuser", "houses", NounForm.PLURAL);
    pairNoun("Katze", "cat", NounForm.SINGULAR);
    pairNoun("Katzen", "cats", NounForm.PLURAL);
    pairNoun("Buch", "book", NounForm.SINGULAR);
    pairNoun("Bücher", "books", NounForm.PLURAL);
    pairNoun("Kind", "child", NounForm.SINGULAR);
    pairNoun("Kinder", "children", NounForm.PLURAL);
    pairNoun("Stadt", "city", NounForm.SINGULAR);
    pairNoun("Städte", "cities", NounForm.PLURAL);
    pairNoun("Baum", "tree", NounForm.SINGULAR);
    pairNoun("Bäume", "trees", NounForm.PLURAL);
    pairNoun("Freund", "friend", NounForm.SINGULAR);
    pairNoun("Freunde", "friends", NounForm.PLURAL);
    pairNoun("Tisch", "table", NounForm.SINGULAR);
    pairNoun("Tische", "tables", NounForm.PLURAL);
    pairNoun("Fenster", "window", NounForm.SINGULAR);
    pairNoun("Fenster", "windows", NounForm.PLURAL);

    Vocable bank = Vocable.noun("Bank", Language.GERMAN, NounForm.SINGULAR);
    bank.associateWith(Vocable.noun("bank", Language.ENGLISH, NounForm.SINGULAR));
    bank.associateWith(Vocable.noun("bench", Language.ENGLISH, NounForm.SINGULAR));
    persistGraph(bank);
  }

  private void seedVerbs() {
    pairVerb("gehen", "go", VerbForm.PRESENT);
    pairVerb("ging", "went", VerbForm.FIRST_PAST);
    pairVerb("gegangen", "gone", VerbForm.SECOND_PAST);
    pairVerb("sehen", "see", VerbForm.PRESENT);
    pairVerb("sah", "saw", VerbForm.FIRST_PAST);
    pairVerb("gesehen", "seen", VerbForm.SECOND_PAST);
    pairVerb("essen", "eat", VerbForm.PRESENT);
    pairVerb("aß", "ate", VerbForm.FIRST_PAST);
    pairVerb("gegessen", "eaten", VerbForm.SECOND_PAST);
    pairVerb("kommen", "come", VerbForm.PRESENT);
    pairVerb("kam", "came", VerbForm.FIRST_PAST);
    pairVerb("gekommen", "come", VerbForm.SECOND_PAST);
    pairVerb("machen", "make", VerbForm.PRESENT);
    pairVerb("machte", "made", VerbForm.FIRST_PAST);
    pairVerb("gemacht", "made", VerbForm.SECOND_PAST);
    pairVerb("schreiben", "write", VerbForm.PRESENT);
    pairVerb("schrieb", "wrote", VerbForm.FIRST_PAST);
    pairVerb("geschrieben", "written", VerbForm.SECOND_PAST);
  }

  private void seedAdjectives() {
    pairAdjective("groß", "big", AdjectiveForm.BASE);
    pairAdjective("größer", "bigger", AdjectiveForm.FIRST_COMPARATIVE);
    pairAdjective("größte", "biggest", AdjectiveForm.SECOND_COMPARATIVE);
    pairAdjective("klein", "small", AdjectiveForm.BASE);
    pairAdjective("kleiner", "smaller", AdjectiveForm.FIRST_COMPARATIVE);
    pairAdjective("kleinste", "smallest", AdjectiveForm.SECOND_COMPARATIVE);
    pairAdjective("schnell", "fast", AdjectiveForm.BASE);
    pairAdjective("schneller", "faster", AdjectiveForm.FIRST_COMPARATIVE);
    pairAdjective("schnellste", "fastest", AdjectiveForm.SECOND_COMPARATIVE);
    pairAdjective("gut", "good", AdjectiveForm.BASE);
    pairAdjective("besser", "better", AdjectiveForm.FIRST_COMPARATIVE);
    pairAdjective("beste", "best", AdjectiveForm.SECOND_COMPARATIVE);
    pairAdjective("schön", "beautiful", AdjectiveForm.BASE);
    pairAdjective("schöner", "more beautiful", AdjectiveForm.FIRST_COMPARATIVE);
    pairAdjective("schönste", "most beautiful", AdjectiveForm.SECOND_COMPARATIVE);
  }

  private void pairNoun(String german, String english, NounForm form) {
    Vocable de = Vocable.noun(german, Language.GERMAN, form);
    de.associateWith(Vocable.noun(english, Language.ENGLISH, form));
    persistGraph(de);
  }

  private void pairVerb(String german, String english, VerbForm form) {
    Vocable de = Vocable.verb(german, Language.GERMAN, form);
    de.associateWith(Vocable.verb(english, Language.ENGLISH, form));
    persistGraph(de);
  }

  private void pairAdjective(String german, String english, AdjectiveForm form) {
    Vocable de = Vocable.adjective(german, Language.GERMAN, form);
    de.associateWith(Vocable.adjective(english, Language.ENGLISH, form));
    persistGraph(de);
  }

  private void persistGraph(Vocable root) {
    root.assertPersistable();
    Set<Vocable> nodes = new LinkedHashSet<>();
    collect(root, nodes);
    Set<String> writtenAssociations = new HashSet<>();
    for (Vocable node : nodes) {
      for (Association association : node.getAssociationsAsGerman()) {
        if (!writtenAssociations.add(association.getId())) {
          continue;
        }
        entityManager
            .createNativeQuery(
                "insert into vocable_association"
                    + " (id, german_vocable_id, english_vocable_id) values (?1, ?2, ?3)")
            .setParameter(1, association.getId())
            .setParameter(2, association.getGerman().getId())
            .setParameter(3, association.getEnglish().getId())
            .executeUpdate();
      }
    }
    for (Vocable node : nodes) {
      node.assertPersistable();
      entityManager
          .createNativeQuery(
              "insert into vocable (id, vocable_text, vocable_text_normalized, language,"
                  + " required_association_id, context, form)"
                  + " values (?1, ?2, ?3, ?4, ?5, ?6, ?7)")
          .setParameter(1, node.getId())
          .setParameter(2, node.getText())
          .setParameter(3, node.getTextNormalized())
          .setParameter(4, node.getLanguage().name())
          .setParameter(5, node.getRequiredAssociation().getId())
          .setParameter(6, node.getContext().name())
          .setParameter(7, node.getForm().name())
          .executeUpdate();
    }
  }

  private void collect(Vocable vocable, Set<Vocable> found) {
    if (!found.add(vocable)) {
      return;
    }
    vocable.getAssociates().forEach(partner -> collect(partner, found));
  }
}
