package run;

import imageMediation.image;
import modsDigester.Mods;
import modsDigester.mvzTaccPage;
import modsDigester.mvzSection;
import modsDigester.modsFactory;

import java.util.Iterator;

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
        Mods mods = new modsFactory(xmlFile).getMods(false);

        // Loop through each section for this XML file
        Iterator sectionsIt = mods.getSections().iterator();
        while (sectionsIt.hasNext()) {
            mvzSection section = (mvzSection) sectionsIt.next();
            Iterator pagesIt = section.getPages().iterator();
            // Loop through pages
            while (pagesIt.hasNext()) {
                mvzTaccPage page = (mvzTaccPage) pagesIt.next();
                image image = new image(page);
                image.writeAllScales();
                image.close();
            }
        }
    }
}
