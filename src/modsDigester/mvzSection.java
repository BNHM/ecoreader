package modsDigester;

import imageMediation.image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import renderer.pageMetadata;
import renderer.sectionMetadata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class mvzSection implements sectionMetadata {
    private String title;
    private String identifier;
    private String geographic;
    private String dateCreated;
    private final LinkedList<Term> nameList = new LinkedList<Term>();
    private final LinkedList<Term> familyNameList = new LinkedList<Term>();
    private final LinkedList<pageMetadata> pages = new LinkedList<pageMetadata>();


    public String getGeographic() {
        return geographic;
    }

    public void addGeographic(String geographic) {
        this.geographic = geographic;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    @Override
    public Integer getSectionNumber() {
        return Integer.parseInt(getSectionNumberAsString().substring(1));
    }

    @Override
    public String getSectionNumberAsString() {
        String[] chunks = identifier.split("/");
        String lastchunk = chunks[chunks.length - 1];
        return lastchunk.split("_")[1];
    }

    public void addDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void addTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getNameText() {
        return modsUtils.getTermValue(nameList, "type", "given");
    }

    public LinkedList<Term> getNameList() {
        return nameList;
    }

    public void setName(Term term) {
        nameList.addLast(term);
    }

    public String getFamilyNameText() {
        return modsUtils.getTermValue(familyNameList, "type", "family");
    }

    public LinkedList<Term> getFamilyNameList() {
        return familyNameList;
    }

    public void setFamilyName(Term term) {
        familyNameList.addLast(term);
    }

    /**
     * Look for cached sampled image files locally
     *
     * @param identifier
     */
    public void addLocalIdentifier(String identifier) {
        this.identifier = identifier;
        String[] chunks = identifier.split("/");
        String volume = chunks[chunks.length - 1].split("_")[0];
        addPages(image.imageDirectory + File.separator + volume + File.separator + image.PAGE);
    }

    /**
     * Look for image files on remote server
     *
     * @param identifier
     */
    public void addIdentifier(String identifier) {
        // Strip .jpg off of name... this is not required but is in the identifier
        identifier = identifier.replace(".jpg", "");

        this.identifier = identifier;
        // Add pages when fetching the identifier, the assumption
        // is that the Identifier for the Section contains a directory
        // listing of pages within the section at the URL from the identifer
        addPages(identifier);
    }

    public LinkedList<pageMetadata> getPages() {
        return pages;
    }

    public void addPage(pageMetadata page) {
        pages.addLast(page);
    }

    /**
     * Add pages to this section
     *
     * @param urlString URL string for a section that contains pages
     *                  Which assumes that this URL contains a list of pages as images that
     *                  exist in a particular form, e.g. "v1316_s1_p001.tif"
     *                  NOTE that this is a brittle approach and it assumes
     *                  ALOT about the server and the naming of the files!
     */
    public void addPages(String urlString) {
        Document doc = null;
        try {
            if (urlString.contains("http")) {
                doc = Jsoup.connect(urlString).timeout(60000).get();
                for (Element file : doc.select("td a")) {
                    String filename = file.attr("href");
                    if (filename.contains("tif") || filename.contains("TIF")) {
                        addPage(new mvzTaccPage(identifier, filename));
                        //System.out.print("+");
                    }
                }
            } else {
                File f = new File(urlString);
                ArrayList<File> files = new ArrayList<File>(java.util.Arrays.asList(f.listFiles()));
                java.util.Iterator filesIt = files.iterator();
                while (filesIt.hasNext()) {
                    File file = ((File) filesIt.next());
                    addPage(new mvzTaccPage(file.getAbsolutePath(), file.getName()));
                }
            }
        } catch (IOException e) {
            //System.out.print("-");
   //         System.out.println("   404:" + urlString);
        }

    }

}
