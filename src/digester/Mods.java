package digester;

import renderer.NotebookMetadata;

import java.util.Iterator;
import java.util.LinkedList;

public class Mods implements NotebookMetadata {

    private int id;
    private Bar bar;
    private String title;

    private final LinkedList<Bar> bars = new LinkedList<Bar>();
    private final LinkedList<Term> languageList = new LinkedList<Term>();
    private final LinkedList<Term> nameList = new LinkedList<Term>();

    private String identifier;

    public void setLanguage(Term term) {
        languageList.addLast(term);
    }

    public LinkedList<Term> getLanguage() {
        return languageList;
    }

    public String getLanguageText() {
        return getTermValue(languageList, "text");
    }

    public void addBar(Bar bar) {
        bars.addLast(bar);
    }

    public LinkedList<Bar> getBars() {
        return bars;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getNameText() {
        return getTermValue(nameList, "family");
    }

    public LinkedList<Term> getNameList() {
        return nameList;
    }

    public void setName(Term term) {
        nameList.addLast(term);
    }

    /**
     * Generic method for fetching values for Terms in the MOD file
     *
     * @param terms
     * @param attributeType
     *
     * @return
     */
    private String getTermValue(LinkedList<Term> terms, String attributeType) {
        Iterator termsIterator = terms.iterator();
        while (termsIterator.hasNext()) {
            Term t = (Term) termsIterator.next();
            if (t.getType().equals(attributeType)) {
                return t.getValue();
            }
        }
        return null;
    }
}
