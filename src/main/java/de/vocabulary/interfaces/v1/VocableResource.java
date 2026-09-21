package de.vocabulary.interfaces.v1;

import de.vocabulary.domain.Context;
import de.vocabulary.domain.Language;
import de.vocabulary.service.VocableService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/vocables")
@Produces(MediaType.APPLICATION_JSON)
public class VocableResource {

  private final VocableService vocables;

  @Inject
  public VocableResource(VocableService vocables) {
    this.vocables = vocables;
  }

  @GET
  @Path("/random")
  public VocableJson random(
      @NotNull @QueryParam("language") Language language,
      @NotNull @QueryParam("context") Context context) {
    return vocables
        .findRandom(language, context)
        .map(VocableJson::from)
        .orElseThrow(() -> new VocableNotFoundException(language, context));
  }

  @POST
  @Path("/{id}/score")
  @Consumes(MediaType.APPLICATION_JSON)
  public ScoreJson score(@PathParam("id") String id, @Valid @NotNull ScoreRequest request) {
    return vocables
        .score(id, request.translation())
        .map(ScoreJson::from)
        .orElseThrow(() -> new VocableNotFoundException(id));
  }
}
