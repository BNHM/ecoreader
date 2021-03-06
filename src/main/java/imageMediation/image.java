package imageMediation;

//import com.sun.media.jai.codec.FileSeekableStream;
//import com.sun.media.jai.codec.TIFFDecodeParam;
import modsDigester.mvzTaccPage;
import org.apache.commons.io.FileUtils;
import utils.SettingsManager;

import javax.imageio.ImageIO;
//import javax.media.jai.JAI;
//import javax.media.jai.RenderedOp;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.imgscalr.Scalr.*;

/**
 * Image class for working with Images and writing various output formats
 */
public class image {
    // the image we are working with
    BufferedImage image;
    // place to hold tmpFile
    File tmpFile;

    mvzTaccPage page;
    String volume;

    public static int THUMB = 200;
    public static int PAGE = 600;
    public static int BIG = 1200;

    public static String imageDirectory;
    public static String format = "png";

    private boolean exists = false;

    /**
     * Construct the image class with an individual page
     *
     * @param page
     */
    public image(mvzTaccPage page) throws Exception {
        SettingsManager sm = SettingsManager.getInstance();
        sm.loadProperties();
        this.imageDirectory = sm.retrieveValue("imageFilePath");

        this.page = page;
        volume = page.getVolume();

        // Create the output directory path with volume directory and subdirectories for various resolutions
        // Check to see if this exists
        String outputFileString = imageDirectory +
                File.separator + volume +
                File.separator + BIG +
                File.separator + page.getName() +
                "." + format;

        if (new File(outputFileString).exists()) {
            exists = true;
        } else {
            System.out.println("Copying " + page.getFullPath());

            // Create a temporary file
            // TODO: implement more robust tmpfile method
            tmpFile = new File("web" + File.separator + imageDirectory + File.separator + "tmpfile");


            // TODO: remove this comment block

            try {
                System.out.println("Copying to local ...");
                FileUtils.copyURLToFile(new URL(page.getFullPath()), tmpFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             *
            * NOTE: JBD Removing this in January, 2020 when porting to new Java... this part will need
             * a replacement...

            // Create the filestream for reading the file we've copied over from the remote server
            FileSeekableStream stream = null;
            try {
                stream = new FileSeekableStream(tmpFile);
            } catch (IOException e) {
                throw new Exception(e);
            }

            // Decode the TIFF and create an image object
            TIFFDecodeParam decodeParam = new TIFFDecodeParam();
            decodeParam.setDecodePaletteAsShorts(true);
            ParameterBlock params = new ParameterBlock();
            params.add(stream);
            RenderedOp image1 = JAI.create("tiff", params);
            image = image1.getAsBufferedImage();
            */

        }
    }

    /**
     * Tells us if the image exists in the local file system
     *
     * @return
     */
    public boolean getExists() {
        return exists;
    }

    public void writeAllScales() {
        writeNewSize(THUMB);
        writeNewSize(BIG);
        writeNewSize(PAGE);
    }

    private String writeNewSize(int targetSize) {

        System.out.println("writing thumbnail ...");

        // Create the output directory path with volume directory and subdirectories for various resolutions
        String outputFileString = imageDirectory +
                File.separator + volume +
                File.separator + targetSize +
                File.separator + page.getName() +
                "." + format;

        // Create the output file
        File outputfile = new File(outputFileString);

        // Make all required directories if they don't exist
        outputfile.mkdirs();

        // Resize the image
        BufferedImage t = resize(image, Method.SPEED, targetSize, OP_ANTIALIAS, OP_BRIGHTER);

        try {
            ImageIO.write(t, format, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created " + outputfile.getAbsolutePath());

        // Return the file that was created
        return outputfile.getAbsolutePath();
    }

    /**
     * main method used for local testing
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String[] names = ImageIO.getWriterFormatNames();
        System.out.println("format names:\n");
        for (int i = 0; i < names.length; i++) {

            System.out.println("\t" + names[i]);
        }


        mvzTaccPage page = new mvzTaccPage("http://web.corral.tacc.utexas.edu/MVZ/fieldnotes/GrinnellJ/v1316_s2/", "v1316_s2_p000.tif");
        image i = new image(page);

        i.writeAllScales();
        i.close();
    }

    /**
     * Cleanup, close tmpFile
     */
    public void close() {
        System.out.println("Cleaning up.");
        // TODO: remove this comment!
        //tmpFile.delete();
    }
}
