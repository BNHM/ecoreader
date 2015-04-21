package renderer;
/**
 * Printer class is meant to output whatever format you want.
 * Individual methods are called to return the desired format, reading instances of NotebookMetadata
 */
public class printer {

    NotebookMetadata notebook = null;

    /**
     * Create printer object by passing in an instance of NotebookMetadata object
     * @param notebook
     */
    public printer(NotebookMetadata notebook) {
        this.notebook = notebook;
    }

    /**
     * Returning a delimited text output of results with delimiter type specified as method argument.
     * First line is always header.
     *
     * @param delimiter
     * @return
     */
    public String getDelimitedText(String delimiter) {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("language" + delimiter);
        sb.append("identifier" + delimiter);
        sb.append("name");

        // Next line
        sb.append("\n");

        // Build output format
        sb.append(notebook.getLanguageText() + delimiter);
        sb.append(notebook.getIdentifier() + delimiter);
        sb.append(notebook.getNameText());

        return sb.toString();
    }
}
