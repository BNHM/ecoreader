package renderer;

import imageMediation.image;
import modsDigester.mvzSection;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * print notebook as JSON
 */
public class jsonPrinter extends  printerAbstractClass {

    String delimiter;

    /**
     * Create printer object by passing in an instance of NotebookMetadata object
     *
     * @param notebook
     */
    public jsonPrinter(NotebookMetadata notebook, String delimiter) {
        super(notebook);
        this.delimiter = delimiter;
    }

    /**
     * Print all the pages out by section in JSON
     *
     * @param section
     *
     * @return
     */
    public String printPages(sectionMetadata section) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t\"pages\": [\n");

        // Loop pages
        LinkedList<pageMetadata> pages = section.getPages();
        Iterator pagesIt = pages.iterator();
        while (pagesIt.hasNext()) {
            pageMetadata page = (pageMetadata) pagesIt.next();
            sb.append(printPage(page));
            if (pagesIt.hasNext()) {
                sb.append(",\n");
            }
        }
        sb.append("\n\t\t\t]\n");
        return sb.toString();
    }

    /**
     * Print an individual page as JSON
     *
     * @param page
     *
     * @return
     */
    public String printPage(pageMetadata page) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\t\t{\n");
        sb.append("\t\t\t\t\t\"pageSmall\":\"" + page.getImageLocation(image.THUMB) + "\",\n");
        sb.append("\t\t\t\t\t\"pageMedium\":\"" + page.getImageLocation(image.PAGE) + "\",\n");
        sb.append("\t\t\t\t\t\"pageLarge\":\"" + page.getImageLocation(image.BIG) + "\",\n");
        sb.append("\t\t\t\t\t\"pageNumber\":\"" + page.getPageNumberAsInt() + "\",\n");
        sb.append("\t\t\t\t\t\"@id\":\"" + page.getFullPath() + "\",\n");
        sb.append("\t\t\t\t\t\"@type\":\"http://purl.org/dc/dcmitype/Image\"\n");
        sb.append("\t\t\t\t}");
        return sb.toString();
    }

    /**
     * Print an individual section metadata as JSON
     *
     * @param section
     *
     * @return
     */
    public String printSection(sectionMetadata section) {
        StringBuilder sb = new StringBuilder();
        ;
        sb.append("\t\t{\n");
        sb.append("\t\t\t\"@id\":\"" + section.getIdentifier() + "\",\n");
        sb.append("\t\t\t\"@type\":\"http://purl.org/dc/dcmitype/Collection\",\n");
        sb.append("\t\t\t\"title\":\"" + section.getTitle() + "\",\n");
        sb.append("\t\t\t\"geographic\":\"" + section.getGeographic() + "\",\n");
        sb.append("\t\t\t\"dateCreated\":\"" + section.getDateCreated() + "\",\n");
        sb.append("\t\t\t\"sectionNumberAsString\":\"" + section.getSectionNumberAsString() + "\",\n");

        sb.append(printPages(section));
        sb.append("\t\t}");
        return sb.toString();
    }

    /**
     * Print out metadata about sections with this notebook
     */
    public String printSections() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\"sections\": [\n");
        LinkedList<sectionMetadata> sections = notebook.getSections();
        // Loop sections
        Iterator sectionsIt = sections.iterator();
        while (sectionsIt.hasNext()) {
            mvzSection section = (mvzSection) sectionsIt.next();
            sb.append(printSection(section));
            if (sectionsIt.hasNext()) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("\t]");
        return sb.toString();
    }

    /**
     * Print out the entire notebook structure
     *
     * @return
     */
    public String printAllNotebookMetadata() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(printNotebookElements() + ",\n");
        sb.append(printSections() + ",\n");
        sb.append(printAllNotebookElementsContext());
        sb.append("\n}");
        return sb.toString();
    }

    /**
     * Print out metadata about notebook itself
     *
     * @return
     */
    public String printNotebookMetadata() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n" + printNotebookElements() + ",\n");
        sb.append(printNotebookElementsContext() + "}");

        return sb.toString();
    }

    /**
     * Print out the notebook elements
     *
     * @return
     */
    private String printNotebookElements() {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("\t\"@id\":\"" + notebook.getIdentifier() + "\",\n");
        sb.append("\t\"@type\":\"http://purl.org/dc/terms/BibliographicResource\",\n");
        sb.append("\t\"title\":\"" + notebook.getTitle() + "\",\n");
        sb.append("\t\"startDate\":\"" + notebook.getDateStartText() + "\",\n");
        sb.append("\t\"endDate\":\"" + notebook.getDateEndText() + "\",\n");
        sb.append("\t\"name\":\"" + notebook.getNameText() + "\"");

        return sb.toString();
    }

    private String printNotebookElementsContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\"@context\": {\n");
        sb.append("\t\t\"title\":\"http://purl.org/dc/elements/1.1/title\",\n");
        sb.append("\t\t\"name\":\"http://purl.org/dc/elements/1.1/creator\",\n");
        sb.append("\t\t\"startDate\":\"http://purl.org/dc/terms/issued\",\n");
        sb.append("\t\t\"endDate\":\"http://purl.org/dc/terms/issued\"\n");
        sb.append("\t}\n");
        return sb.toString();
    }

    private String printAllNotebookElementsContext() {
        StringBuilder sb = new StringBuilder();

        // Volume level context
        sb.append("\t\"@context\": {\n");
        sb.append("\t\t\"title\":\"http://purl.org/dc/elements/1.1/title\",\n");
        sb.append("\t\t\"name\":\"http://purl.org/dc/elements/1.1/creator\",\n");
        sb.append("\t\t\"startDate\":\"http://purl.org/dc/terms/issued\",\n");
        sb.append("\t\t\"endDate\":\"http://purl.org/dc/terms/issued\",\n");

        // Section level context
        sb.append("\t\t\"sections\":{\n");
        sb.append("\t\t\t\"@id\":\"http://purl.org/dc/terms/hasPart\",\n");
        sb.append("\t\t\t\"@type\":\"@id\"\n");
        sb.append("\t\t},\n");

        // Page level content
        sb.append("\t\t\"pages\":{\n");
        sb.append("\t\t\t\"@id\":\"http://purl.org/dc/terms/hasPart\",\n");
        sb.append("\t\t\t\"@type\":\"@id\"\n");
        sb.append("\t\t}\n");

        sb.append("\t}\n");


        return sb.toString();
    }
}
