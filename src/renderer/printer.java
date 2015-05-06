package renderer;

import modsDigester.mvzTaccPage;
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

    /**
     * Print all the pages out by section
     * @param sectionNumber
     * @return
     */
    public String printPages(int sectionNumber) {


        StringBuilder sb = new StringBuilder();

        // Find the section by the section number index
        // TODO: find a better way to do this... actually should find section by the prescribed section number not the linkedlist order!
        Section section = notebook.getSections().get(sectionNumber);//.getFirst();

        // get all the pages associated with this section
        LinkedList<mvzTaccPage> pages = section.getPages();

        Iterator pagesIt = pages.iterator();
        while (pagesIt.hasNext()) {
            mvzTaccPage page = (mvzTaccPage) pagesIt.next();
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
