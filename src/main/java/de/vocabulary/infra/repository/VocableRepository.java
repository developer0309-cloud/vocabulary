package de.vocabulary.infra.repository;

import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.entity.VocableEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

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
}
