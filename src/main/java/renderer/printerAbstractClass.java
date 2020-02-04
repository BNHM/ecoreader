package renderer;

/**
 * Printer abstract class is the base format for printing output in a variety of formats,
 * as defined by subClasses (e.g. JSON, mysql inserts, delimited, etc.)
 */
public abstract class printerAbstractClass {

    NotebookMetadata notebook = null;


    public printerAbstractClass(NotebookMetadata notebook) {
        this.notebook = notebook;
    }

    /**
     * Print all the pages out by section in JSON
     *
     * @param section
     *
     * @return
     */
    public String printPages(sectionMetadata section) {
        return null;
    }

    /**
     * Print an individual page as JSON
     *
     * @param page
     *
     * @return
     */
    public String printPage(pageMetadata page) {
        return null;
    }

    /**
     * Print an individual section metadata as JSON
     *
     * @param section
     *
     * @return
     */
    public String printSection(sectionMetadata section) {
        return null;
    }

    /**
     * Print out metadata about sections with this notebook
     */
    public String printSections() {
        return null;
    }

    /**
     * Print out the entire notebook structure
     *
     * @return
     */
    public String printAllNotebookMetadata() {
        return null;
    }

    /**
     * Print out metadata about notebook itself
     *
     * @return
     */
    public String printNotebookMetadata() {
        return null;
    }

}
