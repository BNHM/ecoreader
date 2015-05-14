package renderer;

/**
 * A generic interface for describe page level metadata.  This assumes the page, of course, is an image
 * living somewhere in digital format and not the page itself.  Implementing classes can focus on various
 * implementations of where these digital "pages" are found and how they are stored, including implementing
 * details about how page number, volume, and sections are stored in the filename, an associated database,
 * or other mechanims.
 */
public interface pageMetadata {

    /**
     * Get the full path where this page lives
     *
     * @return
     */
    String getFullPath();

    /**
     * Get the image INPUT filename
     *
     * @return
     */
    public String getImageFileInputName();

    /**
     * Location of image, with size as parameter
     *
     * @return
     */
    public String getImageLocation(int size);

    /**
     * Get the page number as an integer
     *
     * @return
     */
    public int getPageNumberAsInt();

    /**
     * Return the Page Number as a String
     *
     * @return
     */
    public String getPageNumberAsString();

    /**
     * Get the image name itself, minus the format extension
     *
     * @return
     */
    public String getName();

    /**
     * Return the volume that this page is associated with
     *
     * @return
     */
    public String getVolume();

}
