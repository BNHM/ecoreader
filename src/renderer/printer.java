package renderer;

import modsDigester.mvzSection;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Printer class is meant to output whatever format you want.
 * Individual methods are called to return the desired format, reading instances of NotebookMetadata
 */
public class printer {

    NotebookMetadata notebook = null;
    String delimiter;

    /**
     * Create printer object by passing in an instance of NotebookMetadata object
     *
     * @param notebook
     */
    public printer(NotebookMetadata notebook, String delimiter) {
        this.delimiter = delimiter;
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
        sb.append("\t\t\t\t{");
        sb.append("\"pageImageName\":\"" + page.getImageFileName() + "\",");
        sb.append("\"pageNumber\":\"" + page.getPageNumberAsInt() + "\"");
        sb.append("}");
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
        sb.append("\t\t\t\"identifier\":\"" + section.getIdentifier() + "\",\n");
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
     * @return
     */
    public String printAllNotebookMetadata() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(printNotebookElements() + ",\n");
        sb.append(printSections());
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

        sb.append("{\n" + printNotebookElements() + "\n}");

        return sb.toString();
    }

    /**
     * Print out the notebook elements
     * @return
     */
    private String printNotebookElements() {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("\t\"title\":\"" + notebook.getTitle() + "\",\n");
        sb.append("\t\"identifier\":\"" + notebook.getIdentifier() + "\",\n");
        sb.append("\t\"startDate\":\"" + notebook.getDateStartText() + "\",\n");
        sb.append("\t\"endDate\":\"" + notebook.getDateEndText() + "\",\n");
        sb.append("\t\"name\":\"" + notebook.getNameText() + "\"");

        return sb.toString();
    }
}
