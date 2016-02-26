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
    //@Path("/{author}")
    public Response getVolumesByAuthor(@QueryParam("author") String author,
                                       @QueryParam("begin_date") int begin_date,
                                       @QueryParam("end_date") int end_date,
                                       @QueryParam("section_title") String section_title,
                                       @QueryParam("volume_id") int volume_id,
                                       @DefaultValue("false") @QueryParam("scanned_only") boolean scanned_only) {
        ecoReader er = new ecoReader();

        // parse (or not) author name
        String familyName = "";
        String givenName = "";
        if (author !=null && !author.trim().equals("")) {
            // Limit the split to just two elements-- useful in cases where given name has a comma in it
            String[] names = author.split(",",2);
            familyName = names[0].trim();
            givenName = names[1].trim();
        } else {
            familyName = null;
            givenName = null;
        }

        String json = er.getVolumes(familyName, givenName, section_title, scanned_only, volume_id, begin_date, end_date);

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

