package run;

import modsDigester.*;
import renderer.jsonPrinter;

/**
 * natureReader class contains the nuts and bolt functions to parse XML files for Field Notebooks
 * in various formats.  Currently, MODS is the only supported format but the system is
 * designed to be extensible to any other formats (MARC, Dublin Core, etc).
 *
 * This class is the primary entry point to the application when testing in the development
 * environment or running from the command-line.  It may be superseded by REST services.
 */
public class ecoReader {

    /**
     * Main method for command-line testing
     *
     * @param args
     */
    public static void main(String[] args) {

        // Here is a test file to work with.
        // Later, we want to harvest any docs that appear in GitHub repository and put in Mysql database
        String testFile = "file:docs/mvz/mods/Grinnell_v1316_MODS.xml";

        // Create mods object to hold MODS data
        Mods mods = new modsFactory(testFile).getMods();

        // Create an instance of printer with MODS object
        jsonPrinter printer = new jsonPrinter(mods,"|");

        // Get output in a particular format... this can be any type of format defined in the printer object
        //System.out.println( printer.printNotebookMetadata());

        System.out.println( printer.printAllNotebookMetadata());
    }
}
