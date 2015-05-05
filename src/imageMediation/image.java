package imageMediation;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import modsDigester.Page;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import static org.imgscalr.Scalr.*;

/**
 * Image class for working with Images and writing various output formats
 */
public class image {
    // the image we are working with
    BufferedImage image;
    // place to hold tmpFile
    File tmpFile;
    Page page;
    public static int THUMB = 200;
    public static int PAGE = 800;
    public static int BIG = 2000;


    public image(Page page) {
        this.page = page;
        System.out.println("Copying " + page.getFullPath());

        // Create a temporary file
        // TODO: implement more robust tmpfile method
        tmpFile = new File("imagethumbs/tmpfile");

        /*try {
            System.out.println("Copying to local ...");
            FileUtils.copyURLToFile(new URL(page.getFullPath()), tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }   */

        FileSeekableStream stream = null;
        try {
            stream = new FileSeekableStream(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TIFFDecodeParam decodeParam = new TIFFDecodeParam();
        decodeParam.setDecodePaletteAsShorts(true);
        ParameterBlock params = new ParameterBlock();
        params.add(stream);
        RenderedOp image1 = JAI.create("tiff", params);
        image = image1.getAsBufferedImage();
    }

    public void writeAllScales() {
        writeNewSize(THUMB);
        writeNewSize(BIG);
        writeNewSize(PAGE);
    }

    private String writeNewSize(int targetSize) {
        String format = "png";

        System.out.println("writing thumbnail ...");

        String outputFileString = "imagethumbs/" + targetSize + "/" + page.getName() + "_" + targetSize + "." + format;

        File outputfile = new File(outputFileString);

        BufferedImage t = resize(image, Method.SPEED, targetSize, OP_ANTIALIAS, OP_BRIGHTER);

        try {
            ImageIO.write(t, format, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created " + outputfile.getAbsolutePath());

        return outputfile.getAbsolutePath();
    }

    public static void main(String[] args) {

        //image i = new image();
        Page page = new Page("http://web.corral.tacc.utexas.edu/MVZ/fieldnotes/GrinnellJ/v1316_s1/", "v1316_s1_p000.tif");

        image i = new image(page);
        i.writeAllScales();
        // i.close();


    }

    /**
     * Cleanup, close tmpFile
     */
    public void close() {
        System.out.println("Cleaning up.");
        tmpFile.delete();
    }
}
