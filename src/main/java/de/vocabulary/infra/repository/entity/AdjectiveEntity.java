package de.vocabulary.infra.repository.entity;

import de.vocabulary.domain.AdjectiveForm;
import de.vocabulary.domain.Language;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("ADJECTIVE")
public class AdjectiveEntity extends VocableEntity {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "form", nullable = false, length = 32)
  private AdjectiveForm form;

  protected AdjectiveEntity() {}

  public AdjectiveEntity(
      String id, String text, String textNormalized, Language language, AdjectiveForm form) {
    super(id, text, textNormalized, language);
    this.form = form;
  }

  public AdjectiveForm getForm() {
    return form;
  }
}
