package rest;

import run.ecoReader;
import utils.database;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.sql.Connection;

/**
 * REST services for working with authors
 *
 * @author jdeck
 */

@Path("authors")
@Produces(MediaType.APPLICATION_JSON)
public class authors {
    Response.ResponseBuilder rb;

    @GET
    @Path("/list")
    @Produces("application/json")
    public Response list() {

        ecoReader ecoReader = new ecoReader();
        String json = ecoReader.getAuthors();

        try {
            rb = Response.ok(json);
        } catch (Exception e) {
            rb = Response.status(204);
            rb.header("Access-Control-Allow-Origin", "*");
            return rb.build();
        }
        rb.header("Access-Control-Allow-Origin", "*");
        return rb.build();

    }
}
