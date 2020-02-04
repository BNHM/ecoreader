package rest;

import run.ecoReader;

import java.net.MalformedURLException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * REST services for working with sections
 *
 */
@Path("pages")
public class pages{
    ResponseBuilder rb;

//    @GET
//    @Produces("application/json")
//    @Path("/list")
//    public Response getSections(
//            @QueryParam("session") String session) throws MalformedURLException {
//
//
//        ecoReader er = new ecoReader();
//        String json = er.getSections(null);
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
    @Path("/{section}/")
    public Response getPagesBySection(@PathParam("section") String section) {
        ecoReader er = new ecoReader();
        String json = er.getPages(section);

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

