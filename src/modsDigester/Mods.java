package modsDigester;

import renderer.NotebookMetadata;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * MODS class is an instance of NotebookMetadata and meant to be a representation of
 * a MODS data file, for use in the natureReader library.  The MODS class is meant to be
 * built using the Apache Digester functions with definitions derived from the Digester
 * library.
 */
public class Mods implements NotebookMetadata {

    // xmlFile to Parse
    public String inputXmlFile;

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
     * Generic method for fetching values for Terms in the MODS file.
     * This class is a work-around for MODS data files which store various
     * data in repeating elements with attributes defining entity types.  Since
     * Apache Digester cannot parse this data, we instead build a linkedList
     * of "terms" and pass in an attribute type, which we can search for to
     * output the appropriate value.
     *
     * @param terms
     * @param attributeType
     *
     * @return
     */
    public String getTermValue(LinkedList<Term> terms, String attributeType) {
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