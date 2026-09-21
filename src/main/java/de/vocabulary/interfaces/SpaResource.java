package de.vocabulary.interfaces;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

/** Serves the React SPA for client-side routes under the HTTP root. */
@Path("/")
public class SpaResource {

  @GET
  @Path("{page:select|learn|empty|end}")
  @Produces(MediaType.TEXT_HTML)
  public Response page() {
    InputStream html =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("META-INF/resources/index.html");
    if (html == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(html).type(MediaType.TEXT_HTML_TYPE.withCharset("UTF-8")).build();
  }
}
