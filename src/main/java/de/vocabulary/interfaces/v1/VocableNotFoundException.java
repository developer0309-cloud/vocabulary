package de.vocabulary.interfaces.v1;

import de.vocabulary.domain.Context;
import de.vocabulary.domain.Language;

/** No vocable exists for the requested language and context. */
public class VocableNotFoundException extends RuntimeException {

  public VocableNotFoundException(Language language, Context context) {
    super("No vocable found for language " + language + " and context " + context);
  }

  public VocableNotFoundException(String vocableId) {
    super("No vocable found with id " + vocableId);
  }
}
