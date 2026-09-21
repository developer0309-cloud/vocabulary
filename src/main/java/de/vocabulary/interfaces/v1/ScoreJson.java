package de.vocabulary.interfaces.v1;

import de.vocabulary.domain.Vocable;
import de.vocabulary.service.TranslationScore;
import java.util.Comparator;
import java.util.List;

public record ScoreJson(boolean correct, List<VocableJson.AssociateJson> associates) {

  static ScoreJson from(TranslationScore score) {
    Vocable vocable = score.vocable();
    List<VocableJson.AssociateJson> associates =
        vocable.getAssociates().stream()
            .map(VocableJson.AssociateJson::from)
            .sorted(
                Comparator.comparing(VocableJson.AssociateJson::text, String.CASE_INSENSITIVE_ORDER))
            .toList();
    return new ScoreJson(score.correct(), associates);
  }
}
