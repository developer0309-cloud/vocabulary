package de.vocabulary.infra.repository.entity;

import de.vocabulary.domain.Language;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "vocable",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_vocable_disjoint",
            columnNames = {"vocable_text_normalized", "language", "context", "form"}),
    indexes =
        @Index(name = "idx_vocable_language_context", columnList = "language, context"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "context", discriminatorType = DiscriminatorType.STRING, length = 32)
public abstract class VocableEntity {

  @Id
  @Column(name = "id", nullable = false, length = 36)
  private String id;

  @NotBlank
  @Column(name = "vocable_text", nullable = false, length = 255)
  private String text;

  @NotBlank
  @Column(name = "vocable_text_normalized", nullable = false, length = 255)
  private String textNormalized;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "language", nullable = false, length = 32)
  private Language language;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "required_association_id", nullable = false)
  private AssociationEntity requiredAssociation;

  @OneToMany(mappedBy = "german", cascade = CascadeType.PERSIST)
  private Set<AssociationEntity> associationsAsGerman = new HashSet<>();

  @OneToMany(mappedBy = "english", cascade = CascadeType.PERSIST)
  private Set<AssociationEntity> associationsAsEnglish = new HashSet<>();

  protected VocableEntity() {}

  protected VocableEntity(String id, String text, String textNormalized, Language language) {
    this.id = id;
    this.text = text;
    this.textNormalized = textNormalized;
    this.language = language;
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public String getTextNormalized() {
    return textNormalized;
  }

  public Language getLanguage() {
    return language;
  }

  public AssociationEntity getRequiredAssociation() {
    return requiredAssociation;
  }

  public void setRequiredAssociation(AssociationEntity requiredAssociation) {
    this.requiredAssociation = requiredAssociation;
  }

  public Set<AssociationEntity> getAssociationsAsGerman() {
    return associationsAsGerman;
  }

  public Set<AssociationEntity> getAssociationsAsEnglish() {
    return associationsAsEnglish;
  }
}
