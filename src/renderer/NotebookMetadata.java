package renderer;

import modsDigester.mvzSection;

import java.util.LinkedList;

/**
 * Generic interface for defining Notebook Metadata elements
 * this is used by Renderer classes and reading class both so that we
 * can expect a common way of dealing with expected metadata elements
 */
public interface NotebookMetadata {
    /**
     * Text version of the language
     * @return
     */
    String getLanguageText();

    /**
     * Document title
     * @return
     */
    String getTitle();

    /**
     * Identifier denotes the location of the image
     * @return
     */
    String getIdentifier();

    /**
     * the name of the Author
     * @return
     */
    String getNameText();

    String getDateStartText();

    String getDateEndText();

    LinkedList<sectionMetadata> getSections();
}
