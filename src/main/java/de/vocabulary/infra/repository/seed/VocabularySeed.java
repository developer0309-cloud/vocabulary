package de.vocabulary.infra.repository.seed;

import de.vocabulary.domain.AdjectiveForm;
import de.vocabulary.domain.Language;
import de.vocabulary.domain.NounForm;
import de.vocabulary.domain.VerbForm;
import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.VocableGraphWriter;
import de.vocabulary.infra.repository.VocableRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Loads a German–English catalog covering every context and form. Associations
 * are inserted before vocables so {@code required_association_id} can be set.
 */
@ApplicationScoped
public class VocabularySeed {

  private final VocableRepository vocables;
  private final VocableGraphWriter graphs;

  @Inject
  public VocabularySeed(VocableRepository vocables, VocableGraphWriter graphs) {
    this.vocables = vocables;
    this.graphs = graphs;
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
    graphs.persist(root);
  }
}
