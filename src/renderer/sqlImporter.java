package renderer;

import modsDigester.Mods;
import modsDigester.modsFactory;
import modsDigester.mvzSection;
import utils.ServerErrorException;
import utils.database;

import java.io.File;
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
    StringBuilder errors = new StringBuilder();


    public sqlImporter() {
        db = new database();
        conn = db.getConn();
    }


    /**
     * Given a list of mods files, validate them, appending to error string
     *
     * @param files
     *
     * @return
     */
    public String validateNotebooks(List<String> files, boolean ignoreSections) {
        for (String file : files) {
            // Create mods object to hold MODS data
            modsFactory factory = new modsFactory(file);
            factory.setIgnoreSections(ignoreSections);
            Mods mods = factory.getMods();
            validateNotebook(mods);
        }
        return errors.toString();
    }

    /**
     * given a list of mods files, import them into the db
     *
     * @param files
     */
    public void importNotebooks(List<String> files) throws validationException {

        for (String file : files) {
            // Create mods object to hold MODS data
            Mods mods = new modsFactory(file).getMods();


            try {
                importNotebook(mods);
            } catch (validationException e) {
                // do nothing, i think i want to catch
                //e.printStackTrace();
            }
        }
        if (!errors.toString().equals("")) {
            throw new validationException(errors.toString());
        }

    }

    /**
     * Import a given mods file into the db
     *
     * @param notebook
     */
    public void importNotebook(NotebookMetadata notebook) throws validationException {
        this.notebook = notebook;

        if (validateNotebook(notebook)) {
            saveVolume();

            for (sectionMetadata section : notebook.getSections()) {
                mvzSection mvzSection = (mvzSection) section;
                saveSection(mvzSection);

                for (pageMetadata page : mvzSection.getPages()) {
                    savePage(page, mvzSection.getIdentifier());
                }
            }
        } else {
            throw new validationException("One or more files did not process:\n" + errors.toString());
        }
    }

    /**
     * Validate a given notebook
     */
    private boolean validateNotebook(NotebookMetadata notebook) {
        System.out.println("checking into notebook ");
        System.out.println("\tfilename " + notebook.getFilename());
        System.out.println("\tidentifier " + notebook.getIdentifier());
        if (notebook.getIdentifier() == null) {
            errors.append(notebook.getFilename() + " has no Volume Identifier\n");
        }
        if (notebook.getFamilyNameText() == null) {
            errors.append(notebook.getFilename() + " has no Familyname \n");
        }
        if (!errors.toString().equals("")) return false;
        else return true;
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
        String sql = null;
        try {
             sql = "INSERT INTO page (section_id, page_number, page_identifier, type) VALUES ((SELECT section_id " +
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
            System.out.println(sql);
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    private void saveVolume() {
        PreparedStatement stmt = null;
        String sql = null;
        try {
            sql = "REPLACE INTO volume (volume_identifier, type, title, startDate, endDate, family_name, given_name, filename) VALUES (" +
                    "?,?,?,?,?,?,?,?)";
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
            System.out.println(sql);
            e.printStackTrace();
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    /**
     * given a list of mods files, update the stored mods file
     *
     * @param files
     */
    public void updateNotebooks(List<String> files) throws validationException {
        for (String file : files) {
            // Create mods object to hold MODS data
            Mods mods = new modsFactory(file).getMods();

            try {
                updateNotebook(mods);
            } catch (validationException e) {
                // do nothing, i think i want to catch
                //e.printStackTrace();
            }
        }
        if (!errors.toString().equals("")) {
            throw new validationException(errors.toString());
        }

    }


    private void updateNotebook(NotebookMetadata notebook) throws validationException {
        this.notebook = notebook;


        if (validateNotebook(notebook)) {
            updateVolume();

            for (sectionMetadata section : notebook.getSections()) {
                mvzSection mvzSection = (mvzSection) section;
                updateSection(mvzSection);

                for (pageMetadata page : mvzSection.getPages()) {
                    updatePage(page, mvzSection.getIdentifier());
                }
            }
        } else {
            throw new validationException("One or more files did not process:\n" + errors.toString());
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
        String sql = null;
        try {
             sql = "UPDATE page SET page_identifier = ?, type = ? WHERE section_id = (SELECT section_id " +
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
            System.out.println(sql + page.getFullPath() + ";null;" + section_identifier + ";" + page.getPageNumberAsInt());
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    /**
     * given a list of mods files, delete them from the db
     *
     * @param files
     */
    public void removeNotebooks(List<String> files) {
        for (String file : files) {
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
     *
     * @param filename
     *
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
     *
     * @param files list of notebook files not to be deleted
     */
    private void removeNotebooksNotInList(List<String> files) {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM volume WHERE volume_id not in (SELECT volume_id FROM (SELECT volume_id FROM volume WHERE filename IN (";

            for (String file : files) {
                sql += "?, ";
            }
            sql = sql.substring(0, sql.lastIndexOf(","));
            sql += ")) as tmp)";

            stmt = conn.prepareStatement(sql);

            int i = 1;
            for (String file : files) {
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
     * This method is called by the python script. The files will be imported or updated. Any mods file that are not
     * included as an arg will be deleted from the database.
     *
     * @param args any mods file to be imported/updated
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("mods files required as argument");
            return;
        }
        ArrayList<String> filenames = new ArrayList<String>();

        sqlImporter im = new sqlImporter();

        String directoryPath = args[0];
        File dir = new File(directoryPath);
        ArrayList<String> filesAsStrings = new ArrayList<String>();
        if (dir.isDirectory()) {
            System.out.println("validating all files in " + dir.getAbsolutePath());
            for (File child : dir.listFiles()) {
                filenames.add(im.processFile(child));
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
            filenames.add(im.processFile(new File(directoryPath)));
            filesAsStrings.add(directoryPath);
        }

        // Delete any mods files in the db that are no longer on github
        if (!filenames.isEmpty()) {
            im.removeNotebooksNotInList(filenames);
        }

    }

    /**
     * Process a file, returning the filename itself as a String (parsed from File object)
     * @param f
     * @return
     * @throws validationException
     */
    private String processFile(File f) throws validationException {
        System.out.println("Processing: " + f.toString());


        Mods mods = new modsFactory("file:///" + f.getAbsolutePath()).getMods();

        String[] filepath = f.getAbsolutePath().split("/");
        String filename = filepath[filepath.length - 1];


        if (notebookExists(filename)) {
            System.out.println("Updating: " + f.getAbsolutePath());
            updateNotebook(mods);
        } else {
            System.out.println("Importing: " + f.getAbsolutePath());
            importNotebook(mods);
        }
        return filename;
    }
}
