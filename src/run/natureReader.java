package run;

import modsDigester.*;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;
import renderer.printer;

import java.io.IOException;

/**
 * natureReader class contains the nuts and bolt functions to parse XML files for Field Notebooks
 * in various formats.  Currently, MODS is the only supported format but the system is
 * designed to be extensible to any other formats (MARC, Dublin Core, etc).
 *
 * This class is the primary entry point to the application when testing in the development
 * environment or running from the command-line.  It may be superseded by REST services.
 */
public class natureReader {

    static String xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/sampledata/test.xml";

    /**
     * Main method for command-line testing
     *
     * @param args
     */
    public static void main(String[] args) {

        Mods mods = new modsFactory(xmlFile).getMods();

        String output = new printer(mods).getDelimitedText("|");

        System.out.println(output);
    }
}
