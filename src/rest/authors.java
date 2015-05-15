package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

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

        String json = "{\n";
        json += "\t\"authors\": [\n";
        json += "\t\t{\n";
        json += "\t\t\t\"id\":\"1\",\n";
        json += "\t\t\t\"name\":\"Grinnell, Joseph\"\n";
        json += "\t\t},";
        json += "\t\t{\n";
        json += "\t\t\t\"id\":\"2\",\n";
        json += "\t\t\t\"name\":\"Leopold, Aldo\"\n";
        json += "\t\t}";
        json += "\t]\n";
        json += "}";
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
