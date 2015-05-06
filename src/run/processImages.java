package run;

import imageMediation.image;
import modsDigester.Mods;
import modsDigester.Page;
import modsDigester.Section;
import modsDigester.modsFactory;
import renderer.printer;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class is the mechanism for building images at various resolutions for use in the
 * natureReader library
 */
public class processImages {

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

        Iterator sectionsIt = mods.getSections().iterator();
        while (sectionsIt.hasNext()) {
            Section section = (Section) sectionsIt.next();
            Iterator pagesIt = section.getPages().iterator();
            while (pagesIt.hasNext()) {
                Page page = (Page) pagesIt.next();
                image image = new image(page);
                image.writeAllScales();
                image.close();
            }
        }
    }
}
