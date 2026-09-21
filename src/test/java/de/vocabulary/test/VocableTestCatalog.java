package de.vocabulary.test;

import de.vocabulary.domain.Language;
import de.vocabulary.domain.NounForm;
import de.vocabulary.domain.VerbForm;
import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.VocableGraphWriter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VocableTestCatalog {

  private final EntityManager entityManager;
  private final VocableGraphWriter graphs;

  @Inject
  public VocableTestCatalog(EntityManager entityManager, VocableGraphWriter graphs) {
    this.entityManager = entityManager;
    this.graphs = graphs;
  }

  @Transactional
  public void deleteAll() {
    entityManager.createNativeQuery("delete from vocable").executeUpdate();
    entityManager.createNativeQuery("delete from vocable_association").executeUpdate();
  }

  @Transactional
  public Vocable persistNounSingular(String german, String english) {
    Vocable de = Vocable.noun(german, Language.GERMAN, NounForm.SINGULAR);
    de.associateWith(Vocable.noun(english, Language.ENGLISH, NounForm.SINGULAR));
    graphs.persist(de);
    return de;
  }

  @Transactional
  public Vocable persistVerbPresent(String german, String english) {
    Vocable de = Vocable.verb(german, Language.GERMAN, VerbForm.PRESENT);
    de.associateWith(Vocable.verb(english, Language.ENGLISH, VerbForm.PRESENT));
    graphs.persist(de);
    return de;
  }
}
