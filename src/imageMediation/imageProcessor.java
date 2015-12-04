package imageMediation;

import modsDigester.Mods;
import run.processImages;

/**
 * a runnable task to invoke the writeImagesForAllSections method of the processImages class
 */
public class imageProcessor implements Runnable {

    private Mods mods;

    public imageProcessor(Mods mods) {
        this.mods = mods;
    }

    @Override
    public void run() {
        processImages processImages = new processImages();
        processImages.writeImagesForAllSections(mods);

    }
}
