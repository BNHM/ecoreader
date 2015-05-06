package renderer;

import modsDigester.Page;
import modsDigester.Section;

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

    public String printPages(Section section) {
        StringBuilder sb = new StringBuilder();

        LinkedList<Page> pages = section.getPages();

        Iterator pagesIt = pages.iterator();
        while (pagesIt.hasNext()) {
            Page page = (Page) pagesIt.next();
            sb.append(section.getIdentifier() + page.getImageFileName());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Print out metadata about sections with this notebook
     */
    public String printSectionMetadata() {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("identifier" + delimiter);
        sb.append("title" + delimiter);
        sb.append("geographic" + delimiter);
        sb.append("dateCreated");

        sb.append("\n");

        LinkedList<Section> sections = notebook.getSections();
        Iterator sectionsIt = sections.iterator();
        while (sectionsIt.hasNext()) {
            Section section = (Section) sectionsIt.next();
            sb.append(section.getIdentifier() + "|");
            sb.append(section.getTitle() + "|");
            sb.append(section.getGeographic() + "|");
            sb.append(section.getDateCreated());
            sb.append("\n");
        }

        return sb.toString();

    }

    /**
     * Print out metadata about notebook itself
     *
     * @return
     */
    public String printNotebookMetadata() {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("title" + delimiter);
        sb.append("identifier" + delimiter);
        sb.append("startDate" + delimiter);
        sb.append("endDate" + delimiter);
        sb.append("name");

        // Next line
        sb.append("\n");

        // Build output format
        sb.append(notebook.getTitle() + delimiter);
        sb.append(notebook.getIdentifier() + delimiter);
        sb.append(notebook.getDateStartText() + delimiter);
        sb.append(notebook.getDateEndText() + delimiter);
        sb.append(notebook.getNameText());

        return sb.toString();
    }
}
