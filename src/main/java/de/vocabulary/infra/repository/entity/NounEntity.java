package de.vocabulary.infra.repository.entity;

import de.vocabulary.domain.Language;
import de.vocabulary.domain.NounForm;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("NOUN")
public class NounEntity extends VocableEntity {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "form", nullable = false, length = 32, columnDefinition = "varchar(32)")
  private NounForm form;

  protected NounEntity() {}

  public NounEntity(
      String id, String text, String textNormalized, Language language, NounForm form) {
    super(id, text, textNormalized, language);
    this.form = form;
  }

  public NounForm getForm() {
    return form;
  }
}
