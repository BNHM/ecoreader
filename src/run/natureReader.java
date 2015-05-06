package run;

import modsDigester.*;
import renderer.printer;

/**
 * natureReader class contains the nuts and bolt functions to parse XML files for Field Notebooks
 * in various formats.  Currently, MODS is the only supported format but the system is
 * designed to be extensible to any other formats (MARC, Dublin Core, etc).
 *
 * This class is the primary entry point to the application when testing in the development
 * environment or running from the command-line.  It may be superseded by REST services.
 */
public class natureReader {

    //static String xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/sampledata/test.xml";
    static String xmlFile = "file:////Users/jdeck/IdeaProjects/nature-reader/docs/mvz/mods/Grinnell_v1316.xml";

    /**
     * Main method for command-line testing
     *
     * @param args
     */
    public static void main(String[] args) {

        // Create mods object to hold MODS data
        Mods mods = new modsFactory(xmlFile).getMods();

        // Create an instance of printer with MODS object
        printer printer = new printer(mods,"|");

        // Get output in a particular format... this can be any type of format defined in the printer object
        //System.out.println( printer.printNotebookMetadata());

        System.out.println( printer.printAllNotebookMetadata());

    }
}
