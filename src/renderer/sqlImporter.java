package renderer;

import modsDigester.mvzSection;
import utils.ServerErrorException;
import utils.SettingsManager;
import utils.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by rjewing on 7/7/15.
 */
public class sqlImporter {

    protected Connection conn;
    database db;
    private NotebookMetadata notebook;

    public sqlImporter(NotebookMetadata notebook) {
        this.notebook = notebook;
        db = new database();
        conn = db.getConn();
    }

    public void importNotebook() {
        saveVolume();

        for (sectionMetadata section: notebook.getSections()) {
            mvzSection mvzSection = (mvzSection) section;
            saveSection(mvzSection);

            for (pageMetadata page: mvzSection.getPages()) {
                savePage(page, mvzSection.getIdentifier());
            }
        }
    }

    private void saveSection(mvzSection section) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO section (volume_id, section_identifier, type, title, geographic, dateCreated, " +
                    "sectionNumberAsString) VALUES ((Select volume_id from volume where volume_identifier = ?),?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());
            stmt.setString(2, section.getIdentifier());
            // TODO insert type
            stmt.setString(3, null);
            stmt.setString(4, section.getTitle());
            stmt.setString(5, section.getGeographic());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date(sdf.parse(section.getDateCreated()).getTime());

            stmt.setDate(6, date);
            stmt.setString(7, section.getSectionNumberAsString());

            stmt.execute();
        } catch (SQLException | ParseException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    private void savePage(pageMetadata page, String section_identifier) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO page (section_id, page_number, page_identifier, type) VALUES ((SELECT section_id " +
                    "FROM section WHERE section_identifier = ?),?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, section_identifier);
            stmt.setInt(2, page.getPageNumberAsInt());
            // is this correct?
            stmt.setString(3, page.getFullPath());
            // TODO insert type
            stmt.setString(4, null);

            stmt.execute();
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    private void saveVolume() {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO volume (volume_identifier, type, title, startDate, endDate, name) VALUES (" +
                    "?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, notebook.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date startDate = new Date(sdf.parse(notebook.getDateStartText()).getTime());
            Date endDate = new Date(sdf.parse(notebook.getDateEndText()).getTime());

            stmt.setDate(4, startDate);
            stmt.setDate(5, endDate);
            stmt.setString(6, notebook.getNameText());

            stmt.execute();
        } catch (SQLException | ParseException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }
}
