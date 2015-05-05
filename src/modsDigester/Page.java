package modsDigester;

/**
 * A page is represented by the imagename that is stored
 * on a remote server.  This class is a utility class for working with
 * those results.
 */
public class Page {
    private String imagename;
    private String pageHome;
    private int pageNumber;


    public Page(String pageHome, String imagename) {
        this.imagename = imagename;
        this.pageHome = pageHome;
    }
    public String getFullPath() {
        return pageHome + imagename;
    }
    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
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

    public String getPageNumberAsString() {
        return getImagename().split("_")[2].split("\\.")[0].substring(1);
    }

    public String getName() {
        return getImagename().split("\\.")[0];
    }

    public static void main(String args[]) {

        /*Page page = new Page("v1316_s1_p001.tif");
        // Find the 3rd element using underscores, then the first part before .
        System.out.println(page.getPageNumberAsString());
        System.out.println(page.getPageNumberAsInt());
        */



    }
}
