package modsDigester;

import renderer.pageMetadata;

/**
 * A page is represented by the imagename that is stored
 * on the remote server, located at TACC  This class is a utility class for working with
 * those results and carries a number of assumptions about how that server is configured, permissions
 * for viewing directories, how those directories are rendered using the remote web service, and the structure
 * of the file names listed on the server
 */
public class mvzTaccPage implements pageMetadata {
    private String imageFileName;
    private String imageFilePath;

    /**
     * Create a page object by passing in its home and the name of the image which represents this page
     *
     * @param imageFilePath
     * @param imageFileName
     */
    public mvzTaccPage(String imageFilePath, String imageFileName) {
        this.imageFileName = imageFileName;
        this.imageFilePath = imageFilePath;
    }

    public String getFullPath() {
        return imageFilePath + imageFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
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
        return getImageFileName().split("_")[2].split("\\.")[0].substring(1);
    }

    /**
     * Get the image name itself, minus the format extension
     *
     * @return
     */
    public String getName() {
        return getImageFileName().split("\\.")[0];
    }

    /**
     * Return the volume that this page is associated with
     *
     * @return
     */
    public String getVolume() {
        return getImageFileName().split("_")[0];
    }

}
