package renderer;

/**
 * Created by jdeck on 4/20/15.
 */
public class printer {

    NotebookMetadata notebook = null;

    public printer(NotebookMetadata notebook) {
        this.notebook = notebook;
    }

    public String getTabFormat() {
        StringBuilder sb = new StringBuilder();

        // Build output format
        sb.append(notebook.getLanguageText() + "\n");
        sb.append(notebook.getIdentifier() + "\n");
        sb.append(notebook.getNameText() + "\n");


        return sb.toString();
    }
}
