package modsDigester;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import renderer.pageMetadata;
import renderer.sectionMetadata;

import java.io.IOException;
import java.util.LinkedList;

public class mvzSection implements sectionMetadata {
    private String title;
    private String identifier;
    private String geographic;
    private String dateCreated;
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
        String[] chunks =  identifier.split("/");
                String lastchunk = chunks[chunks.length -1];
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

    public void addIdentifier(String identifier) {
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
            doc = Jsoup.connect(urlString).timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Element file : doc.select("td a")) {
            String filename = file.attr("href");
            if (filename.contains("tif") || filename.contains("TIF")) {
                addPage(new mvzTaccPage(identifier, filename));
            }
        }
    }

}
