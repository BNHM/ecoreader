package renderer;

import modsDigester.Mods;
import modsDigester.modsFactory;
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
import java.util.List;

/**
 * class to import, update, and remove mods files from the db
 */
public class sqlImporter {

    protected Connection conn;
    database db;
    private NotebookMetadata notebook;

    public sqlImporter() {
        db = new database();
        conn = db.getConn();
    }

    /**
     * given a list of mods files, import them into the db
     * @param files
     */
    public void importNotebooks(List<String> files) {
        for (String file: files) {
            // Create mods object to hold MODS data
            Mods mods = new modsFactory(file).getMods();

            importNotebook(mods);
        }
    }

    /**
     * Import a given mods file into the db
     * @param notebook
     */
    public void importNotebook(NotebookMetadata notebook) {
        this.notebook = notebook;
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

            stmt.setInt(6, Integer.parseInt(section.getDateCreated()));
            stmt.setString(7, section.getSectionNumberAsString());

            stmt.execute();
        } catch (Exception e) {
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
            String sql = "INSERT INTO volume (volume_identifier, type, title, startDate, endDate, name, filename) VALUES (" +
                    "?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, notebook.getTitle());

            stmt.setInt(4, Integer.parseInt(notebook.getDateStartText()));
            stmt.setInt(5, Integer.parseInt(notebook.getDateEndText()));
            stmt.setString(6, notebook.getNameText());
            stmt.setString(7, notebook.getFilename());

            stmt.execute();
        } catch (Exception e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    /**
     * given a list of mods files, update the stored mods file
     * @param files
     */
    public void updateNotebooks(List<String> files) {
        for (String file: files) {
            // Create mods object to hold MODS data
            Mods mods = new modsFactory(file).getMods();

            updateNotebook(mods);
        }
    }

    private void updateNotebook(NotebookMetadata notebook) {
        this.notebook = notebook;
        updateVolume();

        for (sectionMetadata section: notebook.getSections()) {
            mvzSection mvzSection = (mvzSection) section;
            updateSection(mvzSection);

            for (pageMetadata page: mvzSection.getPages()) {
                updatePage(page, mvzSection.getIdentifier());
            }
        }
    }

    private void updateVolume() {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE volume SET volume_identifier = ?, type = ?, title = ?, startDate = ?, endDate = ?," +
                    " name = ? WHERE filename = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, notebook.getTitle());

            stmt.setInt(4, Integer.parseInt(notebook.getDateStartText()));
            stmt.setInt(5, Integer.parseInt(notebook.getDateEndText()));
            stmt.setString(6, notebook.getNameText());
            stmt.setString(7, notebook.getFilename());

            stmt.execute();
        } catch (Exception e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    private void updateSection(mvzSection section) {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE section SET section_identifier = ?, type = ?, title = ?, geographic = ?," +
                    " dateCreated = ? WHERE volume_id = (Select volume_id from volume where filename = ?)" +
                    " AND sectionNumberAsString = ? ";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, section.getIdentifier());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, section.getTitle());
            stmt.setString(4, section.getGeographic());

            stmt.setInt(5, Integer.parseInt(section.getDateCreated()));
            stmt.setString(6, notebook.getFilename());
            stmt.setString(7, section.getSectionNumberAsString());

            stmt.execute();
        } catch (Exception e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    
    private void updatePage(pageMetadata page, String section_identifier) {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE page SET page_identifier = ?, type = ? WHERE section_id = (SELECT section_id " +
                    "FROM section WHERE section_identifier = ?) AND page_number = ?";
            stmt = conn.prepareStatement(sql);

            // is this correct?
            stmt.setString(1, page.getFullPath());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, section_identifier);
            stmt.setInt(4, page.getPageNumberAsInt());

            stmt.execute();
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    /**
     * given a list of mods files, delete them from the db
     * @param files
     */
    public void removeNotebooks(List<String> files) {
        for (String file: files) {
            // Create mods object to hold MODS data
            Mods mods = new modsFactory(file).getMods();

            removeNotebook(mods);
        }
    }

    private void removeNotebook(Mods mods) {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM volume WHERE filename = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, mods.getFilename());
            stmt.execute();
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }
}
