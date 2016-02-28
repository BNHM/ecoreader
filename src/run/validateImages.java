package run;

import modsDigester.mvzTaccPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import utils.database;

import java.io.IOException;
import java.sql.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Validate Images that we have in database compared to what is at TACC
 */
public class validateImages {
    Connection conn;
    database db;

    validateImages() {
        db = new database();
        conn = db.getConn();
    }

    public void run() {
        Statement stmt = null;

        String sql = "select s.section_identifier as section_identifier,count(*) as page_count" +
                " from volume v,section s, page p" +
                " where v.volume_id = s.volume_id and s.section_id = p.section_id group by p.section_id";
        try {
            conn.prepareStatement(sql);
            stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Looking at database instance = " + db.getConn().getMetaData().getURL());
            System.out.println("This may take a bit to process.  We will output any sections which image counts do not match ...");

            // Print out list of images that we know about here
            while (rs.next()) {
                String url = rs.getString("section_identifier");
                Integer dbCount = rs.getInt("page_count");

                //System.out.println( + " has " + rs.getString("page_count") + " pages");
                Document doc = Jsoup.connect(rs.getString("section_identifier")).timeout(60000).get();
                Integer taccCount = 0;
                for (Element file : doc.select("td a")) {
                    String filename = file.attr("href");
                    if (filename.contains("tif") || filename.contains("TIF")) {
                        taccCount++;
                    }
                }
                if (!dbCount.equals(taccCount)) {
                    System.out.println(url + " has " + dbCount + " dbCount and " + taccCount + " taccCount");
                }
                //sections.add(rs.getString("section_identifier"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        validateImages v = new validateImages();
        v.run();
    }
}
