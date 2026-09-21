package de.vocabulary.infra.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "vocable_association",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_vocable_association_de_en",
            columnNames = {"german_vocable_id", "english_vocable_id"}))
public class AssociationEntity {

  @Id
  @Column(name = "id", nullable = false, length = 36)
  private String id;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(
      name = "german_vocable_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private VocableEntity german;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(
      name = "english_vocable_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private VocableEntity english;

  protected AssociationEntity() {}

  public AssociationEntity(String id, VocableEntity german, VocableEntity english) {
    this.id = id;
    this.german = german;
    this.english = english;
    german.getAssociationsAsGerman().add(this);
    english.getAssociationsAsEnglish().add(this);
  }

  public String getId() {
    return id;
  }

  public VocableEntity getGerman() {
    return german;
  }

  public VocableEntity getEnglish() {
    return english;
  }
}
