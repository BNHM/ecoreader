package modsDigester;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * The modsFactory classes specifies all of the rules for parsing the MODS file using
 * Apache Digester rules/methods.
 */
public class modsFactory {
    String xmlFile;
    boolean ignoreSections = false;

    public modsFactory(String xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void setIgnoreSections(Boolean value) {
        ignoreSections = value;
    }

    /**
     * Default to NOT looking locally, which makes a BIG assumption that image files are
     * somehow copied over to local server
     * @return
     */
    public Mods getMods() {
        return getMods(false);
    }
    /**
     * Create the Mods object
     *
     * @return
     */
    public Mods getMods(boolean local) {
        Mods mods = null;
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setRuleNamespaceURI("http://www.loc.gov/mods/v3");
        digester.setErrorHandler(new ErrorHandler());

        /**
         * Root MODS element
         */
        digester.addObjectCreate("mods", Mods.class);
        digester.addSetProperties("mods");

        // Identifier
        digester.addCallMethod("mods/identifier", "setIdentifier", 0);

        // Set title
        digester.addCallMethod("mods/titleInfo/title", "setTitle", 0);

        // Language
        digester.addObjectCreate("mods/language/languageTerm", Term.class);
        digester.addSetProperties("mods/language/languageTerm");
        digester.addSetNext("mods/language/languageTerm", "setLanguage");
        digester.addCallMethod("mods/language/languageTerm", "setValue", 0);


        // Name  (only extract family name here)
        digester.addObjectCreate("mods/name/namePart", Term.class);
        digester.addSetProperties("mods/name/namePart");
        digester.addSetNext("mods/name/namePart", "setFamilyName");
        digester.addSetNext("mods/name/namePart", "setName");
        digester.addCallMethod("mods/name/namePart", "setValue", 0);

         // Date Created
        digester.addObjectCreate("mods/originInfo/dateCreated", Term.class);
        digester.addSetProperties("mods/originInfo/dateCreated");
        digester.addSetNext("mods/originInfo/dateCreated", "setDateStart");
        digester.addSetNext("mods/originInfo/dateCreated", "setDateEnd");
        digester.addCallMethod("mods/originInfo/dateCreated", "setValue", 0);

        /**
         * Sub-elements
         */
        if (!ignoreSections) {

            digester.addObjectCreate("mods/relatedItem", mvzSection.class);
            digester.addSetProperties("mods/relatedItem");

            digester.addSetNext("mods/relatedItem", "addSection");


            if (local) {
                digester.addCallMethod("mods/relatedItem/identifier", "addLocalIdentifier", 0);
            } else {
                digester.addCallMethod("mods/relatedItem/identifier", "addIdentifier", 0);
            }
            digester.addCallMethod("mods/relatedItem/titleInfo/title", "addTitle", 0);
            digester.addCallMethod("mods/relatedItem/originInfo/dateCreated", "addDateCreated", 0);
            digester.addCallMethod("mods/relatedItem/subject/geographic", "addGeographic", 0);
        }

        try {
            mods = digester.parse(xmlFile);
            mods.setFilename(xmlFile.split("/")[xmlFile.split("/").length - 1]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return mods;
    }

}
