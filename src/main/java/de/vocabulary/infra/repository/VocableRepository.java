package de.vocabulary.infra.repository;

import de.vocabulary.domain.Context;
import de.vocabulary.domain.Language;
import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.entity.AdjectiveEntity;
import de.vocabulary.infra.repository.entity.NounEntity;
import de.vocabulary.infra.repository.entity.VerbEntity;
import de.vocabulary.infra.repository.entity.VocableEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class VocableRepository {

  private final EntityManager entityManager;
  private final VocablePersistenceMapper mapper = new VocablePersistenceMapper();

  @Inject
  public VocableRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public long count() {
    return entityManager
        .createQuery("select count(v) from VocableEntity v", Long.class)
        .getSingleResult();
  }

  public long count(Language language, Context context) {
    return entityManager
        .createQuery(
            "select count(v) from VocableEntity v"
                + " where v.language = :language and type(v) = :type",
            Long.class)
        .setParameter("language", language)
        .setParameter("type", entityType(context))
        .getSingleResult();
  }

  /**
   * Uniform draw over vocables with the given language and context. Form is
   * whatever the chosen row has. Empty when the catalog has no match.
   */
  @Transactional
  public Optional<Vocable> findRandom(Language language, Context context) {
    long matching = count(language, context);
    if (matching == 0) {
      return Optional.empty();
    }
    int offset = ThreadLocalRandom.current().nextInt(Math.toIntExact(matching));
    TypedQuery<VocableEntity> query =
        entityManager.createQuery(
            "select v from VocableEntity v"
                + " where v.language = :language and type(v) = :type"
                + " order by v.id",
            VocableEntity.class);
    query.setParameter("language", language);
    query.setParameter("type", entityType(context));
    query.setFirstResult(offset);
    query.setMaxResults(1);
    List<VocableEntity> found = query.getResultList();
    if (found.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(mapper.toDomain(found.get(0)));
  }

  public Optional<Vocable> findById(String id) {
    VocableEntity entity = entityManager.find(VocableEntity.class, id);
    if (entity == null) {
      return Optional.empty();
    }
    return Optional.of(mapper.toDomain(entity));
  }

  @Transactional
  public void save(Vocable vocable) {
    vocable.assertPersistable();
    VocableEntity entity = mapper.toEntity(vocable);
    if (entityManager.find(VocableEntity.class, entity.getId()) == null) {
      entityManager.persist(entity);
    } else {
      entityManager.merge(entity);
    }
    for (Vocable partner : vocable.getAssociates()) {
      partner.assertPersistable();
      VocableEntity partnerEntity = mapper.toEntity(partner);
      if (entityManager.find(VocableEntity.class, partnerEntity.getId()) == null) {
        entityManager.persist(partnerEntity);
      } else {
        entityManager.merge(partnerEntity);
      }
    }
  }

  @Transactional
  public void delete(Vocable vocable) {
    Set<Vocable> toDelete = vocable.remove();
    for (Vocable doomed : toDelete) {
      VocableEntity entity = entityManager.find(VocableEntity.class, doomed.getId());
      if (entity != null) {
        entityManager.remove(entity);
      }
    }
  }

  private static Class<? extends VocableEntity> entityType(Context context) {
    return switch (context) {
      case NOUN -> NounEntity.class;
      case VERB -> VerbEntity.class;
      case ADJECTIVE -> AdjectiveEntity.class;
    };
  }
}
