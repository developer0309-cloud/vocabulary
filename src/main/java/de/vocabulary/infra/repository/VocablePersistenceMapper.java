package de.vocabulary.infra.repository;

import de.vocabulary.domain.Adjective;
import de.vocabulary.domain.AdjectiveForm;
import de.vocabulary.domain.Association;
import de.vocabulary.domain.Noun;
import de.vocabulary.domain.NounForm;
import de.vocabulary.domain.Verb;
import de.vocabulary.domain.VerbForm;
import de.vocabulary.domain.Vocable;
import de.vocabulary.infra.repository.entity.AdjectiveEntity;
import de.vocabulary.infra.repository.entity.AssociationEntity;
import de.vocabulary.infra.repository.entity.NounEntity;
import de.vocabulary.infra.repository.entity.VerbEntity;
import de.vocabulary.infra.repository.entity.VocableEntity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibernate.Hibernate;

final class VocablePersistenceMapper {

  Vocable toDomain(VocableEntity entity) {
    Map<String, Vocable> vocables = new HashMap<>();
    Vocable root = toVocable(entity, vocables);
    for (AssociationEntity association : associationsOf(entity)) {
      Vocable german = toVocable(association.getGerman(), vocables);
      Vocable english = toVocable(association.getEnglish(), vocables);
      boolean present =
          german.getAssociationsAsGerman().stream()
              .anyMatch(existing -> existing.getId().equals(association.getId()));
      if (!present) {
        Association.of(association.getId(), german, english);
      }
    }
    return root;
  }

  VocableEntity toEntity(Vocable vocable) {
    vocable.assertPersistable();
    Map<String, VocableEntity> entities = new HashMap<>();
    VocableEntity root = toVocableEntity(vocable, entities);
    Set<String> written = new HashSet<>();
    for (Vocable node : reachable(vocable)) {
      for (Association association : node.getAssociationsAsGerman()) {
        if (!written.add(association.getId())) {
          continue;
        }
        VocableEntity german = toVocableEntity(association.getGerman(), entities);
        VocableEntity english = toVocableEntity(association.getEnglish(), entities);
        AssociationEntity associationEntity =
            new AssociationEntity(association.getId(), german, english);
        if (association.equals(association.getGerman().getRequiredAssociation())) {
          german.setRequiredAssociation(associationEntity);
        }
        if (association.equals(association.getEnglish().getRequiredAssociation())) {
          english.setRequiredAssociation(associationEntity);
        }
      }
    }
    return root;
  }

  private Set<Vocable> reachable(Vocable vocable) {
    Set<Vocable> found = new HashSet<>();
    collect(vocable, found);
    return found;
  }

  private void collect(Vocable vocable, Set<Vocable> found) {
    if (!found.add(vocable)) {
      return;
    }
    vocable.getAssociates().forEach(partner -> collect(partner, found));
  }

  private Set<AssociationEntity> associationsOf(VocableEntity entity) {
    Set<AssociationEntity> associations = new HashSet<>();
    associations.addAll(entity.getAssociationsAsGerman());
    associations.addAll(entity.getAssociationsAsEnglish());
    if (entity.getRequiredAssociation() != null) {
      associations.add(entity.getRequiredAssociation());
    }
    return associations;
  }

  private Vocable toVocable(VocableEntity entity, Map<String, Vocable> cache) {
    entity = Hibernate.unproxy(entity, VocableEntity.class);
    Vocable cached = cache.get(entity.getId());
    if (cached != null) {
      return cached;
    }
    Vocable vocable =
        switch (entity) {
          case NounEntity noun ->
              Vocable.noun(
                  noun.getId(), noun.getText(), noun.getLanguage(), noun.getForm());
          case VerbEntity verb ->
              Vocable.verb(
                  verb.getId(), verb.getText(), verb.getLanguage(), verb.getForm());
          case AdjectiveEntity adjective ->
              Vocable.adjective(
                  adjective.getId(),
                  adjective.getText(),
                  adjective.getLanguage(),
                  adjective.getForm());
          default ->
              throw new IllegalStateException("Unknown vocable entity " + entity.getClass());
        };
    cache.put(entity.getId(), vocable);
    return vocable;
  }

  private VocableEntity toVocableEntity(Vocable vocable, Map<String, VocableEntity> cache) {
    VocableEntity cached = cache.get(vocable.getId());
    if (cached != null) {
      return cached;
    }
    VocableEntity entity =
        switch (vocable) {
          case Noun noun ->
              new NounEntity(
                  noun.getId(),
                  noun.getText(),
                  noun.getTextNormalized(),
                  noun.getLanguage(),
                  (NounForm) noun.getForm());
          case Verb verb ->
              new VerbEntity(
                  verb.getId(),
                  verb.getText(),
                  verb.getTextNormalized(),
                  verb.getLanguage(),
                  (VerbForm) verb.getForm());
          case Adjective adjective ->
              new AdjectiveEntity(
                  adjective.getId(),
                  adjective.getText(),
                  adjective.getTextNormalized(),
                  adjective.getLanguage(),
                  (AdjectiveForm) adjective.getForm());
          default -> throw new IllegalStateException("Unknown vocable " + vocable.getClass());
        };
    cache.put(vocable.getId(), entity);
    return entity;
  }
}
