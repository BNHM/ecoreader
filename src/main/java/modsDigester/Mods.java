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
    private String filename;
    private String title;

    private final LinkedList<sectionMetadata> sections = new LinkedList<sectionMetadata>();
    private final LinkedList<Term> languageList = new LinkedList<Term>();
    private final LinkedList<Term> nameList = new LinkedList<Term>();
    private final LinkedList<Term> familyNameList = new LinkedList<Term>();
    private final LinkedList<Term> dateList = new LinkedList<Term>();


    private String identifier;

    public void setLanguage(Term term) {
        languageList.addLast(term);
    }

    public LinkedList<Term> getLanguage() {
        return languageList;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLanguageText() {
        return modsUtils.getTermValue(languageList, "type", "text");
    }

    public void addSection(mvzSection section) {sections.addLast(section);}

    public LinkedList<sectionMetadata> getSections() {
        return sections;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {this.title = title;}

    public String getIdentifier() {return identifier;}

    public void setIdentifier(String identifier) {this.identifier = identifier;}

    public String getNameText() {
        return modsUtils.getTermValue(nameList, "type", "given");
    }

    public LinkedList<Term> getNameList() {
        return nameList;
    }

    public void setName(Term term) {
        nameList.addLast(term);}

    public String getFamilyNameText() {
        return modsUtils.getTermValue(familyNameList, "type", "family");
    }

    public LinkedList<Term> getFamilyNameList() {
        return familyNameList;
    }

    public void setFamilyName(Term term) {
        familyNameList.addLast(term);
    }

    public LinkedList<Term> getDateList() {
        return dateList;
    }

    public String getDateStartText() {
        return modsUtils.getTermValue(dateList, "point", "start");
    }

    public void setDateStart(Term term) {
        dateList.addLast(term);
    }

    public String getDateEndText() {
        return modsUtils.getTermValue(dateList, "point", "end");
    }

    public void setDateEnd(Term term) {
        dateList.addLast(term);
    }


}