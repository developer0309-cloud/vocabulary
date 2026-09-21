package de.vocabulary.infra.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.vocabulary.domain.Context;
import de.vocabulary.domain.Language;
import de.vocabulary.domain.Vocable;
import de.vocabulary.test.VocableTestCatalog;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class VocableRepositoryRandomTest {

  @Inject VocableRepository vocables;
  @Inject VocableTestCatalog catalog;

  @BeforeEach
  void clearCatalog() {
    catalog.deleteAll();
  }

  @Test
  @TestTransaction
  void findRandomIsEmptyWhenNoVocableMatches() {
    catalog.persistVerbPresent("gehen-test", "go-test");

    assertEquals(0, vocables.count(Language.GERMAN, Context.NOUN));
    assertTrue(vocables.findRandom(Language.GERMAN, Context.NOUN).isEmpty());
  }

  @Test
  @TestTransaction
  void findRandomReturnsAVocableWithRequestedLanguageAndContext() {
    catalog.persistNounSingular("Haus-test", "house-test");
    catalog.persistVerbPresent("gehen-test", "go-test");

    Optional<Vocable> drawn = vocables.findRandom(Language.GERMAN, Context.NOUN);

    assertTrue(drawn.isPresent());
    Vocable vocable = drawn.orElseThrow();
    assertEquals(Language.GERMAN, vocable.getLanguage());
    assertEquals(Context.NOUN, vocable.getContext());
    assertEquals("Haus-test", vocable.getText());
    assertEquals(1, vocable.getAssociates().size());
  }
}
