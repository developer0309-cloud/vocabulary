package de.vocabulary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.vocabulary.domain.Vocable;
import de.vocabulary.test.VocableTestCatalog;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class VocableServiceScoreTest {

  @Inject VocableService vocables;
  @Inject VocableTestCatalog catalog;

  @BeforeEach
  void clearCatalog() {
    catalog.deleteAll();
  }

  @Test
  @TestTransaction
  void scoreIsCorrectForMatchingTranslationIgnoringCase() {
    Vocable german = catalog.persistNounSingular("Baum-test", "tree-test");

    Optional<TranslationScore> score = vocables.score(german.getId(), "  TREE-test  ");

    assertTrue(score.isPresent());
    TranslationScore result = score.orElseThrow();
    assertTrue(result.correct());
    assertEquals(
        "tree-test", result.vocable().getAssociates().iterator().next().getText());
  }

  @Test
  @TestTransaction
  void scoreIsIncorrectForUnknownTranslation() {
    Vocable german = catalog.persistNounSingular("Tisch-test", "table-test");

    Optional<TranslationScore> score = vocables.score(german.getId(), "chair");

    assertTrue(score.isPresent());
    assertFalse(score.orElseThrow().correct());
  }

  @Test
  @TestTransaction
  void scoreIsEmptyWhenVocableDoesNotExist() {
    assertTrue(vocables.score(UUID.randomUUID().toString(), "house").isEmpty());
  }
}
