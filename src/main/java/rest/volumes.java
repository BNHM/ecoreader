package rest;

import run.ecoReader;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.List;
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
                                       @QueryParam("geographies") List<String> geographies,
                                       @DefaultValue("false") @QueryParam("scanned_only") boolean scanned_only) {
        ecoReader er = new ecoReader();

        // parse (or not) author name
        String familyName = "";
        String givenName = "";
        try {
            author = URLDecoder.decode(author,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (author !=null && !author.trim().equals("")) {
            // Limit the split to just two elements-- useful in cases where given name has a comma in it
            String[] names = author.split(",",2);

            familyName = names[0].trim();
            if (names.length > 1)
                givenName = names[1].trim();
            else
                givenName = null;
        } else {
            familyName = null;
            givenName = null;
        }
        String json = "";

        if  ((familyName == null || familyName.trim().equals("")) &&
                (givenName == null || givenName.trim().equals("")) &&
                begin_date == 0 &&
                end_date == 0 &&
                geographies.size() == 0 &&
                (section_title == null || section_title.trim().equals("") )&&
                volume_id == 0) {
            json = "{[]}";
        } else {
             json = er.getVolumes(familyName, givenName, section_title, scanned_only, volume_id, begin_date, end_date, geographies);
        }
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

