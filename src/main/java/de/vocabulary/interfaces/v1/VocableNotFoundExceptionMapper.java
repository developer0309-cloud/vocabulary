package de.vocabulary.interfaces.v1;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class VocableNotFoundExceptionMapper
    implements ExceptionMapper<VocableNotFoundException> {

  @Override
  public Response toResponse(VocableNotFoundException exception) {
    return Response.status(Response.Status.NOT_FOUND)
        .type(MediaType.APPLICATION_JSON)
        .entity(new ErrorJson(exception.getMessage()))
        .build();
  }
}
