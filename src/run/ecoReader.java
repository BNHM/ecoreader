package run;

import imageMediation.image;
import modsDigester.Mods;
import modsDigester.modsFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import renderer.sqlImporter;
import utils.ServerErrorException;
import utils.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * natureReader class contains the nuts and bolt functions to parse XML files for Field Notebooks
 * in various formats.  Currently, MODS is the only supported format but the system is
 * designed to be extensible to any other formats (MARC, Dublin Core, etc).
 *
 * This class is the primary entry point to the application when testing in the development
 * environment or running from the command-line.  It may be superseded by REST services.
 */
public class ecoReader {
    protected Connection conn;
    database db;

    public ecoReader() {
        db = new database();
        conn = db.getConn();
    }

    public String getAuthors() {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        JSONArray authors = new JSONArray();

        try {
            String sql = "SELECT `name` FROM volume GROUP BY `name`";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }

        JSONObject res = new JSONObject();
        res.put("authors", authors);

        return res.toJSONString();
    }

    public String getVolumes(String author) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        JSONArray volumes = new JSONArray();

        try {
            String sql = "SELECT title, volume_id FROM volume WHERE name = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, author);

            rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject volume = new JSONObject();

                volume.put("volume_id", rs.getString("volume_id"));
                volume.put("title", rs.getString("title"));
                volume.put("sections", new JSONArray());

                volumes.add(volume);
            }

        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }

        JSONObject res = new JSONObject();
        res.put("volumes", volumes);

        getSections(res);
        return res.toJSONString();
    }

    private void getSections(JSONObject volumes) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Iterator it = ((JSONArray) volumes.get("volumes")).iterator();
        try {
            while (it.hasNext()) {
                JSONObject vol = (JSONObject) it.next();
                JSONArray sections = (JSONArray) vol.get("sections");

                int vol_id = Integer.parseInt(vol.get("volume_id").toString());

                // TODO need to add a column to db indicating if the section has been scanned or there is only metadata
                // TODO this column should be populate with the upload script
                String sql = "SELECT section_id, title, geographic, " + "" +
                        "CASE WHEN EXISTS (SELECT section_id FROM page WHERE section.section_id = page.section_id) " +
                        "THEN 'TRUE' ELSE 'FALSE' END AS isScanned " +
                        "FROM section WHERE volume_id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, vol_id);

                rs = stmt.executeQuery();

                while (rs.next()) {
                    JSONObject section = new JSONObject();

                    section.put("section_id", rs.getInt("section_id"));
                    section.put("title", rs.getString("title"));
                    section.put("geographic", rs.getString("geographic"));
                    section.put("isScanned", rs.getBoolean("isScanned"));

                    sections.add(section);
                }
            }
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }
    }

    public String getSectionPages(int section_id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONArray pages = new JSONArray();

        try {
            String sql = "SELECT page_identifier, page_number FROM page WHERE section_id = ? ORDER BY page_number";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, section_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String[] pathArray = rs.getString("page_identifier").split("/");
                String file_name = pathArray[pathArray.length - 1].split(".tif")[0] + ".png";
                String volume = file_name.split("_")[0];

                JSONObject page = new JSONObject();
                page.put("thumb", "images/" + volume + "/" + image.THUMB + "/" + file_name);
                page.put("href", "images/" + volume + "/" + image.PAGE+ "/" + file_name);
                page.put("big", "images/" + volume + "/" + image.BIG+ "/" + file_name);
                page.put("title", "page: " + rs.getInt("page_number"));

                pages.add(page);
            }
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }
        JSONObject res = new JSONObject();
        res.put("pages", pages);

        return res.toJSONString();
    }



    public String getSections(String volume) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        JSONArray sections = new JSONArray();

        try {
            String sql = "SELECT s.title as title FROM section as s, volume as v";
            if (volume != null) {
                sql += " WHERE v.title = ? AND s.volume_id = v.volume_id";
            }

            stmt = conn.prepareStatement(sql);

            if (volume != null) {
                stmt.setString(1, volume);
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                sections.add(rs.getString("title"));
            }

        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }

        JSONObject res = new JSONObject();
        res.put("sections", sections);

        return res.toJSONString();
    }

    public String getPages(String section) {
    PreparedStatement stmt = null;
        ResultSet rs = null;

        JSONArray sections = new JSONArray();

        try {
            String sql = "SELECT s.title as title FROM section as s, volume as v";
            if (section != null) {
                sql += " WHERE v.title = ? AND s.volume_id = v.volume_id";
            }

            stmt = conn.prepareStatement(sql);

            if (section != null) {
                stmt.setString(1, section);
            }

            rs = stmt.executeQuery();
            while (rs.next()) {
                sections.add(rs.getString("title"));
            }

        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }

        JSONObject res = new JSONObject();
        res.put("sections", sections);

        return res.toJSONString();
    }

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

//         Create an instance of printer with MODS object
//        jsonPrinter printer = new jsonPrinter(mods,"|");

        // Get output in a particular format... this can be any type of format defined in the printer object
        //System.out.println( printer.printNotebookMetadata());

//        System.out.println( printer.printAllNotebookMetadata());
        sqlImporter sqlImporter = new sqlImporter(mods);
        sqlImporter.importNotebook();
        ecoReader er = new ecoReader();
        System.out.println(er.getSectionPages(4));
    }
}
