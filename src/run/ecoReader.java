package run;

import imageMediation.image;
import modsDigester.Mods;
import modsDigester.modsFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import renderer.jsonPrinter;
import renderer.sqlImporter;
import utils.ServerErrorException;
import utils.SettingsManager;
import utils.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * natureReader class contains the nuts and bolt functions to parse XML files for Field Notebooks
 * in various formats.  Currently, MODS is the only supported format but the system is
 * designed to be extensible to any other formats (MARC, Dublin Core, etc).
 * <p/>
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
            String sql = "SELECT `given_name`, `family_name` FROM section GROUP BY `family_name`,`given_name`";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                authors.add(rs.getString("family_name") + ", " + rs.getString("given_name"));
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

    /**
     * Get Volumes... these queries work closely with the section table, querying information as author, date, etc...
     * from the section table itself and then returning the enclosing volume information.
     * @param familyName
     * @param givenName
     * @param section_title
     * @param scanned_only
     * @param volume_id
     * @param begin_date
     * @param end_date
     * @return
     */
    public String getVolumes(
            String familyName,
            String givenName,
            String section_title,
            boolean scanned_only,
            int volume_id,
            int begin_date,
            int end_date) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        JSONArray volumes = new JSONArray();

        try {
            int paramSet = 0;
            StringBuilder sql = new StringBuilder("SELECT\n\tv.title, v.volume_id\nFROM\n\tvolume v, section s");
            sql.append("\nWHERE v.volume_id = s.volume_id");
            if (familyName != null) {
                if (familyName.equalsIgnoreCase("null")) {
                    sql.append("\n\tAND s.family_name IS NULL ");
                } else {
                    sql.append("\n\tAND s.family_name = ? ");
                    paramSet++;
                }
            }

            if (givenName != null) {
                if (givenName.equalsIgnoreCase("null")) {
                    sql.append("\n\tAND s.given_name IS NULL");
                } else {
                    sql.append("\n\tAND s.given_name = ?");
                    paramSet++;
                }
            }

            // Query based on section title
            if (section_title != null && !section_title.isEmpty()) {
                sql.append("\n\tAND s.title like concat('%',?,'%')");
                paramSet++;
            }

            // Fetch volume_id not using "volume_id" in the database but the last part of the volume_identifier ('e.g. .../v500')
            // But in this case only input the numeric portion of the volume identifier, e.g. "500.
            // 0 means no volume_id
            if (volume_id > 0) {
                sql.append("\n\tAND v.volume_identifier like concat('%/v',?)");
                paramSet++;
            }

            // Queries based on date-- 0 means no date
            if (begin_date > 0) {
                sql.append("\n\tAND s.dateCreated >= ?");
                paramSet++;
            }
            if (end_date > 0) {
                sql.append("\n\tAND s.dateCreated <= ?");
                paramSet++;
            }

            // Grouping by the volume_id removes duplicate records since we don't want every section associated with this
            // author in most cases, we only want to get distinct volumes
            sql.append("\nGROUP BY v.volume_id");

            // DEBUG
            sql.append(" LIMIT 10");
            System.out.println(sql.toString());

            stmt = conn.prepareStatement(sql.toString());
            int curr = 1;


            if (familyName != null && !familyName.equalsIgnoreCase("null")) {
                stmt.setString(curr, familyName);
                curr++;
            }
            if (givenName != null && !givenName.equalsIgnoreCase("null")) {
                stmt.setString(curr, givenName);
                curr++;
            }

            if (curr <= paramSet) {
                if (section_title != null && !section_title.isEmpty()) {
                    stmt.setString(curr, section_title);
                    curr++;
                }
                if (volume_id > 0) {
                    stmt.setInt(curr, volume_id);
                    curr++;
                }
                if (begin_date > 0) {
                    stmt.setInt(curr, begin_date);
                    curr++;
                }
                if (end_date > 0) {
                    stmt.setInt(curr, end_date);
                    curr++;
                }
            }

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

        getSections(res, scanned_only);
        return res.toJSONString();
    }

    private void getSections(JSONObject volumes, boolean scanned_only) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Iterator it = ((JSONArray) volumes.get("volumes")).iterator();
        try {
            while (it.hasNext()) {
                JSONObject vol = (JSONObject) it.next();
                JSONArray sections = (JSONArray) vol.get("sections");

                int vol_id = Integer.parseInt(vol.get("volume_id").toString());

                String sql = "SELECT section_id, title, geographic, " + "" +
                        "CASE WHEN EXISTS (SELECT section_id FROM page WHERE section.section_id = page.section_id) " +
                        "THEN 'TRUE' ELSE 'FALSE' END AS isScanned " +
                        "FROM section WHERE volume_id = ? ORDER BY section_identifier";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, vol_id);

                rs = stmt.executeQuery();

                while (rs.next()) {
                    JSONObject section = new JSONObject();

                    if (!scanned_only || (scanned_only && rs.getBoolean("isScanned"))) {
                        section.put("section_id", rs.getInt("section_id"));
                        section.put("title", rs.getString("title"));
                        section.put("geographic", rs.getString("geographic"));
                        section.put("isScanned", rs.getBoolean("isScanned"));

                        sections.add(section);
                    }
                }

                //remove the volume if there are no scanned sections and scanned_only it true
                if (scanned_only) {
                    if (((JSONArray) vol.get("sections")).isEmpty()) {
                        it.remove();
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }
    }

    public String getSectionPages(int section_id,
                                  boolean defaultToBig) {


        SettingsManager sm = SettingsManager.getInstance();
        sm.loadProperties();
        String imageURLRoot = sm.retrieveValue("imageURLRoot", "");

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
                page.put("thumb", imageURLRoot + "images/" + volume + "/" + image.THUMB + "/" + file_name);
                if (defaultToBig) {
                    page.put("page", imageURLRoot + "images/" + volume + "/" + image.PAGE + "/" + file_name);
                    page.put("href", imageURLRoot + "images/" + volume + "/" + image.BIG + "/" + file_name);
                } else {
                    page.put("href", imageURLRoot + "images/" + volume + "/" + image.PAGE + "/" + file_name);
                    page.put("big", imageURLRoot + "images/" + volume + "/" + image.BIG + "/" + file_name);
                }
                page.put("high_res", rs.getString("page_identifier"));
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
    public static void main(String[] args) throws Exception {
        ecoReader e = new ecoReader();

       /*
       String familyName,
       String givenName,
       String section_title,
       boolean scanned_only,
       int volume_id,
       int begin_date,
       int end_date
       */
        System.out.println(e.getAuthors());
        //String results = e.getVolumes("Alexander","Annie Montague",null,true,0,0,0);//?begin_date=&end_date=&section_title=&volume_id=")
        //String results = e.getVolumes(null, null, "Bernardino", true, 0, 0, 0);//?begin_date=&end_date=&section_title=&volume_id=")
        //System.out.println(results);
    }
}
