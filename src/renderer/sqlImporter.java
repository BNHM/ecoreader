package renderer;

import modsDigester.Mods;
import modsDigester.modsFactory;
import modsDigester.mvzSection;
import utils.ServerErrorException;
import utils.database;

import java.sql.*;
import java.util.ArrayList;
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
                    "sectionNumberAsString) VALUES ((Select volume_id from volume where filename = ?),?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getFilename());
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
            String sql = "INSERT INTO volume (volume_identifier, type, title, startDate, endDate, family_name, given_name, filename) VALUES (" +
                    "?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, notebook.getTitle());

            stmt.setInt(4, Integer.parseInt(notebook.getDateStartText()));
            stmt.setInt(5, Integer.parseInt(notebook.getDateEndText()));
            stmt.setString(6, notebook.getFamilyNameText());
            stmt.setString(7, notebook.getNameText());
            stmt.setString(8, notebook.getFilename());

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
                    " family_name = ?, given_name = ? WHERE filename = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());
            // TODO insert type
            stmt.setString(2, null);
            stmt.setString(3, notebook.getTitle());

            stmt.setInt(4, Integer.parseInt(notebook.getDateStartText()));
            stmt.setInt(5, Integer.parseInt(notebook.getDateEndText()));
            stmt.setString(6, notebook.getFamilyNameText());
            stmt.setString(7, notebook.getNameText());
            stmt.setString(8, notebook.getFilename());

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

    /**
     * check if a notebook exists so we know if we need to import or update
     * @param filename
     * @return
     */
    private boolean notebookExists(String filename) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT count(*) as count FROM volume WHERE filename = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, filename);

            rs = stmt.executeQuery();
            rs.next();

            return (rs.getInt("count") > 0);
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }
    }

    /**
     * remove any notebooks that aren't in the list of files
     * @param files list of notebook files not to be deleted
     */
    private void removeNotebooksNotInList(List<String> files) {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM volume WHERE volume_id not in (SELECT volume_id FROM (SELECT volume_id FROM volume WHERE filename IN (";

            for (String file: files) {
                sql += "?, ";
            }
            sql = sql.substring(0, sql.lastIndexOf(","));
            sql += ")) as tmp)";

            stmt = conn.prepareStatement(sql);

            int i = 1;
            for (String file: files) {
                stmt.setString(i, file);
                i++;
            }

            stmt.execute();
        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    /**
     * This method is called by the python script. The files will be imported or updated. Any mods file that is not
     * included as an arg will be deleted from the database.
     * @param args any mods file to be imported/updated
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("mods files required as argument");
            return;
        }

        sqlImporter im = new sqlImporter();
        List<String> filenames = new ArrayList<String>();

        for (String file: args) {

            Mods mods = new modsFactory(file).getMods();

            String[] filepath = file.split("/");
            String filename = filepath[filepath.length - 1];
            filenames.add(filename);

            if (im.notebookExists(filename)) {
                System.out.println("Updating: " + file);
                im.updateNotebook(mods);
            } else {
                System.out.println("Importing: " + file);
                im.importNotebook(mods);
            }
        }

        // Delete any mods files in the db that are no longer on github
        if (!filenames.isEmpty()) {
            im.removeNotebooksNotInList(filenames);
        }

    }
}
