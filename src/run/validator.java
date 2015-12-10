package run;

import java.io.File;
import java.util.ArrayList;

/**
 * Validate MODS files.  accepts a directory path as an argument, or
 * if the argument is a file it validates just that file.
 */
public class validator {
    public static void main(String[] args) {
        sqlImporter sqlImporter = new sqlImporter();

        String directoryPath = args[0];
        File dir = new File(directoryPath);
        ArrayList<String> filesAsStrings = new ArrayList<String>();
        if (dir.isDirectory()) {
            System.out.println("validating all files in " + dir.getAbsolutePath());
            for (File child : dir.listFiles()) {
                filesAsStrings.add("file:///" + child.getAbsolutePath());
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
            //System.out.println("validating " + directoryPath);
            System.out.println("validating " + directoryPath);
            filesAsStrings.add("file:///" + directoryPath);
        }
        // tell it to ignore sections as second argument
        System.out.println(sqlImporter.validateNotebooks(filesAsStrings, true));
        System.out.println("finished");
    }
}
