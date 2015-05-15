package rest;

import java.net.MalformedURLException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * REST services for working with volumes
 *
 * @author jdeck
 */
@Path("volumes")
public class volumes {
    ResponseBuilder rb;

    @GET
    @Produces("application/json")
    public Response getVolumes(
            @QueryParam("session") String session) throws MalformedURLException {


        try {
            rb = Response.ok("hello");
        } catch (Exception e) {
            rb = Response.status(204);
            rb.header("Access-Control-Allow-Origin", "*");
            return rb.build();
        }
        rb.header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }
}

