package modsDigester;

import renderer.NotebookMetadata;
import renderer.sectionMetadata;

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
    private String title;

    private final LinkedList<sectionMetadata> sections = new LinkedList<sectionMetadata>();
    private final LinkedList<Term> languageList = new LinkedList<Term>();
    private final LinkedList<Term> nameList = new LinkedList<Term>();
    private final LinkedList<Term> dateList = new LinkedList<Term>();


    private String identifier;

    public void setLanguage(Term term) {
        languageList.addLast(term);
    }

    public LinkedList<Term> getLanguage() {
        return languageList;
    }

    public String getLanguageText() {
        return getTermValue(languageList, "type", "text");
    }

    public void addSection(mvzSection section) {
        sections.addLast(section);
    }

    public LinkedList<sectionMetadata> getSections() {
        return sections;
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
        return getTermValue(nameList, "type", "family");
    }

    public LinkedList<Term> getNameList() {
        return nameList;
    }

    public void setName(Term term) {
        nameList.addLast(term);
    }

    public LinkedList<Term> getDateList() {
        return dateList;
    }

    public String getDateStartText() {
        return getTermValue(dateList, "point", "start");
    }

    public void setDateStart(Term term) {
        dateList.addLast(term);
    }

    public String getDateEndText() {
        return getTermValue(dateList, "point", "end");
    }

    public void setDateEnd(Term term) {
        dateList.addLast(term);
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
     * @param attributeKey
     * @param attributeValue
     *
     * @return
     */
    public String getTermValue(LinkedList<Term> terms, String attributeKey, String attributeValue) {
        Iterator termsIterator = terms.iterator();
        while (termsIterator.hasNext()) {
            Term t = (Term) termsIterator.next();

            if (attributeKey.equals("type") &&
                    t.getType().equals(attributeValue)) {
                return t.getValue();
            } else if (attributeKey.equals("point") &&
                    t.getPoint() != null &&
                    t.getPoint().equals(attributeValue)) {
                return t.getValue();
            }
        }
        return null;
    }


}