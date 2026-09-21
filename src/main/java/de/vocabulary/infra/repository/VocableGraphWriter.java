package de.vocabulary.infra.repository;

import de.vocabulary.domain.Association;
import de.vocabulary.domain.Vocable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Inserts an association graph with associations first, then vocables, so
 * {@code required_association_id} can be set.
 */
@ApplicationScoped
public class VocableGraphWriter {

  private final EntityManager entityManager;

  @Inject
  public VocableGraphWriter(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void persist(Vocable root) {
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
