package de.vocabulary.infra.repository.seed;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class VocabularySeedOnStartup {

  private final VocabularySeed seed;
  private final boolean enabled;

  @Inject
  public VocabularySeedOnStartup(
      VocabularySeed seed,
      @ConfigProperty(name = "vocabulary.seed.enabled", defaultValue = "false") boolean enabled) {
    this.seed = seed;
    this.enabled = enabled;
  }

  void onStart(@Observes StartupEvent event) {
    if (enabled) {
      seed.load();
    }
  }
}
