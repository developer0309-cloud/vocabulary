package de.vocabulary.interfaces.v1;

import de.vocabulary.domain.Context;
import de.vocabulary.domain.Language;
import de.vocabulary.domain.Vocable;
import java.util.Comparator;
import java.util.List;

/** JSON representation of a vocable and its partners in the other language. */
public record VocableJson(
    String id,
    String text,
    Language language,
    Context context,
    String form,
    List<AssociateJson> associates) {

  static VocableJson from(Vocable vocable) {
    List<AssociateJson> associates =
        vocable.getAssociates().stream()
            .map(AssociateJson::from)
            .sorted(Comparator.comparing(AssociateJson::text, String.CASE_INSENSITIVE_ORDER))
            .toList();
    return new VocableJson(
        vocable.getId(),
        vocable.getText(),
        vocable.getLanguage(),
        vocable.getContext(),
        vocable.getForm().toString(),
        associates);
  }

  public record AssociateJson(String id, String text, Language language, String form) {

    static AssociateJson from(Vocable vocable) {
      return new AssociateJson(
          vocable.getId(),
          vocable.getText(),
          vocable.getLanguage(),
          vocable.getForm().toString());
    }
  }
}
