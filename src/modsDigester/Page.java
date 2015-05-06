package modsDigester;

/**
 * A page is represented by the imagename that is stored
 * on a remote server.  This class is a utility class for working with
 * those results.
 */
public class Page {
    private String imageFileName;
    private String pageHome;
    private int pageNumber;

    /**
     * Create a page object by passing in its home and the name of the image which represents this page
     * @param pageHome
     * @param imageFileName
     */
    public Page(String pageHome, String imageFileName) {
        this.imageFileName = imageFileName;
        this.pageHome = pageHome;
    }

    public String getFullPath() {
        return pageHome + imageFileName;
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
     * @return
     */
    public String getPageNumberAsString() {
        return getImageFileName().split("_")[2].split("\\.")[0].substring(1);
    }

    /**
     * Get the image name itself, minus the format extension
     * @return
     */
    public String getName() {
        return getImageFileName().split("\\.")[0];
    }

    /**
     * Return the volume
     * @return
     */
    public String getVolume() {
        return getImageFileName().split("_")[0];
    }

    public static void main(String args[]) {

        /*Page page = new Page("v1316_s1_p001.tif");
        // Find the 3rd element using underscores, then the first part before .
        System.out.println(page.getPageNumberAsString());
        System.out.println(page.getPageNumberAsInt());
        */



    }
}
