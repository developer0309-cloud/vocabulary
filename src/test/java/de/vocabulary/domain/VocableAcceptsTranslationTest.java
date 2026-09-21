package de.vocabulary.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VocableAcceptsTranslationTest {

  @Test
  void matchesAssociateTextIgnoringCaseAndTrim() {
    Vocable german = Vocable.noun("Haus", Language.GERMAN, NounForm.SINGULAR);
    german.associateWith(Vocable.noun("house", Language.ENGLISH, NounForm.SINGULAR));

    assertTrue(german.acceptsTranslation("house"));
    assertTrue(german.acceptsTranslation("HOUSE"));
    assertTrue(german.acceptsTranslation("  House  "));
    assertFalse(german.acceptsTranslation("tree"));
    assertFalse(german.acceptsTranslation(" "));
  }
}
