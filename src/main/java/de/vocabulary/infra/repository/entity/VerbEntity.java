package de.vocabulary.infra.repository.entity;

import de.vocabulary.domain.Language;
import de.vocabulary.domain.VerbForm;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("VERB")
public class VerbEntity extends VocableEntity {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "form", nullable = false, length = 32)
  private VerbForm form;

  protected VerbEntity() {}

  public VerbEntity(
      String id, String text, String textNormalized, Language language, VerbForm form) {
    super(id, text, textNormalized, language);
    this.form = form;
  }

  public VerbForm getForm() {
    return form;
  }
}
