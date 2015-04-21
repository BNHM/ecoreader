package modsDigester;

public class Bar {
    private String title;
    private int id;
    private String definition;

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefinition() {
        return definition;
    }

    public void addDefinition(String definition) {
        this.definition = definition;
    }
}
