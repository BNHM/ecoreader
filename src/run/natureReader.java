package run;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;
import digester.Bar;
import digester.Mods;
import digester.ErrorHandler;
import digester.Term;
import renderer.printer;

import java.io.IOException;

/**
 * Run class contains the nuts and bolt functions to read and print parsed XML files.
 * This class is the primary entry point to the application when testing in the development
 * environment or running from the command-line.  It may be superseded by REST services.
 */
public class natureReader {
    String xmlFile;

    public natureReader() {
        //xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/nature-reader/docs/mvz/mods/Grinnell_v1316.xml";
        xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/nature-reader/docs/test.xml";

    }

    /**
     * Create a MODS object using Apache Digester Method
     *
     * @return
     */
    private Mods buildMods() {
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

        // Set title
        digester.addCallMethod("mods/titleInfo/title", "setTitle", 0);

        // Language
        digester.addObjectCreate("mods/language/languageTerm", Term.class);
        digester.addSetProperties("mods/language/languageTerm");
        digester.addSetNext("mods/language/languageTerm", "setLanguage");
        digester.addCallMethod("mods/language/languageTerm", "setValue", 0);

        // Identifier
        digester.addCallMethod("mods/identifier", "setIdentifier", 0);

        // Name  (only extract family name here)
        digester.addObjectCreate("mods/name/namePart", Term.class);
        digester.addSetProperties("mods/name/namePart");
        digester.addSetNext("mods/name/namePart", "setName");
        digester.addCallMethod("mods/name/namePart", "setValue", 0);

        /**
         * Sub-elements
         */
        digester.addObjectCreate("mods/bar", Bar.class);
        digester.addSetProperties("mods/bar");
        digester.addSetNext("mods/bar", "addBar");
        digester.addCallMethod("mods/bar", "addDefinition", 0);


        try {
            mods = digester.parse(xmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return mods;
    }

    /**
     * Main method for command-line testing
     *
     * @param args
     */
    public static void main(String[] args) {

        natureReader nr = new natureReader();

        Mods mods = nr.buildMods();

        String output = new printer(mods).getDelimitedText("|");

        System.out.println(output);

    }
}
