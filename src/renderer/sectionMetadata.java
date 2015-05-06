package renderer;

import java.util.LinkedList;

/**
 * Work with metadata in a section.  A "section" is a subunit of "volume" (represented by NotebookMetadata) and
 * contains
 * multiple "pages" (represented by pageMetadata)
 */
public interface sectionMetadata {
    /**
     * Get the title for this section
     *
     * @return
     */
    public String getTitle();

    /**
     * Add a title for this section
     *
     * @param title
     */
    public void addTitle(String title);

    /**
     * Get the identifier for this section
     *
     * @return
     */
    public String getIdentifier();

    /**
     * Add an identifier for this section
     *
     * @param identifier
     */
    public void addIdentifier(String identifier);

    /**
     * Get all the pages in this section
     *
     * @return
     */
    public LinkedList<pageMetadata> getPages();

    /**
     * Add an individual page to this section
     *
     * @param page
     */
    public void addPage(pageMetadata page);

    /**
     * Add pages to this section
     */
    public void addPages(String urlString);
}
