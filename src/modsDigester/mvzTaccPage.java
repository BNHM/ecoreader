package modsDigester;

import imageMediation.image;
import renderer.pageMetadata;

import java.io.File;

/**
 * A page is represented by the imagename that is stored
 * on the remote server, located at TACC  This class is a utility class for working with
 * those results and carries a number of assumptions about how that server is configured, permissions
 * for viewing directories, how those directories are rendered using the remote web service, and the structure
 * of the file names listed on the server
 */
public class mvzTaccPage implements pageMetadata {
    private String imageFileInputName;
    private String imageFileInputPath;

    // TODO: fix the path root, this should be an ARK
    public static String imageFilePathRoot = "file:///Users/jdeck/IdeaProjects/ecoreader/ecoreader/web/";
    private String imageLocation;

    /**
     * Create a page object by passing in its home and the name of the image which represents this page
     *
     * @param imageFileInputPath
     * @param imageFileInputName
     */
    public mvzTaccPage(String imageFileInputPath, String imageFileInputName) {
        this.imageFileInputName = imageFileInputName;
        this.imageFileInputPath = imageFileInputPath;

    }

    public String getImageLocation(int size) {
        return imageFilePathRoot + getVolume() + File.separator + size + File.separator + getName() + "." + image.format;
    }

    public String getFullPath() {
        return imageFileInputPath + imageFileInputName;
    }

    public String getImageFileInputName() {
        return imageFileInputName;
    }

    public void setImageFileInputName(String imageFileInputName) {
        this.imageFileInputName = imageFileInputName;
    }

    /**
     * Parse imageName in the form "v1316_s1_p001.tif" to extract the page number
     * as an integer
     *
     * @return
     */
    public int getPageNumberAsInt() {
        return Integer.parseInt(getPageNumberAsString());
    }

    /**
     * Return the Page Number as a String
     *
     * @return
     */
    public String getPageNumberAsString() {
        return getImageFileInputName().split("_")[2].split("\\.")[0].substring(1);
    }

    /**
     * Get the image name itself, minus the format extension
     *
     * @return
     */
    public String getName() {
        return getImageFileInputName().split("\\.")[0];
    }

    /**
     * Return the volume that this page is associated with
     *
     * @return
     */
    public String getVolume() {
        return getImageFileInputName().split("_")[0];
    }

}
