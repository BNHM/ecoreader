package rest;

import run.ecoReader;

import java.net.MalformedURLException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

//    @GET
//    @Produces("application/json")
//    @Path("/list")
//    public Response getVolumes(
//            @QueryParam("session") String session) throws MalformedURLException {
//
//
//        ecoReader er = new ecoReader();
//        String json = er.getVolumes(null);
//        try {
//            rb = Response.ok(json);
//        } catch (Exception e) {
//            rb = Response.status(204);
//            rb.header("Access-Control-Allow-Origin", "*");
//            return rb.build();
//        }
//        rb.header("Access-Control-Allow-Origin", "*");
//        return rb.build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{author}/")
    public Response getVolumesByAuthor(@PathParam("author") String author) {
        ecoReader er = new ecoReader();
        String json = er.getVolumes(author);

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
