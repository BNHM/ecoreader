package run;

import modsDigester.Mods;
import modsDigester.modsFactory;
import modsDigester.mvzSection;
import imageMediation.imageProcessor;
import renderer.NotebookMetadata;
import renderer.pageMetadata;
import renderer.sectionMetadata;
import renderer.validationException;
import utils.ServerErrorException;
import utils.database;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Import, update, and remove mods files from the db
 */
public class sqlImporter {

    protected Connection conn;
    database db;
    private NotebookMetadata notebook;
    StringBuilder errors = new StringBuilder();
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);


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
     * import or update a list of mods files
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
     * Import or update a given mods file
     *
     * @param notebook
     */
    public void importNotebook(Mods notebook) throws validationException {
        errors = new StringBuilder();

        this.notebook = notebook;
        imageProcessor imageProcessor = new imageProcessor(notebook);
        if (validateNotebook(notebook)) {
            saveVolume();

            for (sectionMetadata section : notebook.getSections()) {
                mvzSection mvzSection = (mvzSection) section;
                saveSection(mvzSection);

                for (pageMetadata page : mvzSection.getPages()) {
                    savePage(page, mvzSection.getIdentifier());
                }
            }

            // verify that the section_identifiers in the db match the mods file. this method needs to be called
            // after all sections have already been added
            verifySections();

            // fetch any images that still need to be fetched
            executorService.submit(imageProcessor);
        } else {
            System.out.println ("FAILED LOADING: " + notebook.getFilename());
            //throw new validationException("One or more files did not validate:\n" + notebook.getFilename());
        }
    }

    /**
     * Validate a given notebook
     */
    private boolean validateNotebook(NotebookMetadata notebook) {
        //System.out.println("checking into notebook ");
        //System.out.println("\tfilename " + notebook.getFilename());
        ///System.out.println("\tidentifier " + notebook.getIdentifier());
        if (!notebook.getIdentifier().matches("^.*/v[0-9]+$")) {
            errors.append(notebook.getFilename() + " has an invalid Volume Identifier: ("+ notebook.getIdentifier()+")\n");
        }
        if (notebook.getIdentifier() == null) {
            errors.append(notebook.getFilename() + " has no Volume Identifier\n");
        }
        if (notebook.getFamilyNameText() == null) {
            errors.append(notebook.getFilename() + " has no Familyname \n");
        }
        if (notebook.getTitle() == null) {
            errors.append(notebook.getFilename() + " has no Title \n");
        }
        if (notebook.getFamilyNameText() == null) {
            errors.append(notebook.getFilename() + " has no Family Name \n");
        }
        if (notebook.getNameText() == null) {
            errors.append(notebook.getFilename() + " has no Name \n");
        }
        if (notebook.getDateStartText() == null) {
            errors.append(notebook.getFilename() + " has no Start Date Text \n");
        }
        if (notebook.getDateEndText() == null) {
            errors.append(notebook.getFilename() + " has no End Date Text \n");
        }


        if (!errors.toString().equals("")) return false;
        else return true;
    }

    private void saveSection(mvzSection section) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO section " +
                    "(volume_id, section_identifier, type, title, geographic, dateCreated, sectionNumberAsString,family_name,given_name) " +
                    "VALUES ((Select volume_id from volume where filename = ?)," +
                    "?,?,?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "volume_id = VALUES(volume_id), type = VALUES(type), title = VALUES(title), " +
                    "geographic = VALUES(geographic), dateCreated = VALUES(dateCreated), sectionNumberAsString = VALUES(sectionNumberAsString), " +
                    "family_name = VALUES(family_name), given_name = VALUES(given_name)";

            stmt = conn.prepareStatement(sql);
            System.out.println(notebook.getFilename());
            stmt.setString(1, notebook.getFilename());

            stmt.setString(2, section.getIdentifier());
            // TODO insert type
            stmt.setString(3, null);
            stmt.setString(4, section.getTitle());
            stmt.setString(5, section.getGeographic());

            stmt.setInt(6, Integer.parseInt(section.getDateCreated()));
            stmt.setString(7, section.getSectionNumberAsString());
            stmt.setString(8, section.getFamilyNameText());
            stmt.setString(9, section.getNameText());

            stmt.execute();
        } catch (Exception e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    /**
     * the method verifies that the sections in that db match the
     * this method needs to be called after any new sections have already been inserted into the db
     * @param
     */
    private void verifySections() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> sections = new ArrayList<String>();
        try {
            String sql = "SELECT section_identifier FROM section WHERE volume_id = (SELECT volume_id FROM volume WHERE volume_identifier = ?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, notebook.getIdentifier());

            rs = stmt.executeQuery();

            while (rs.next()) {
                sections.add(rs.getString("section_identifier"));
            }

            // now that we have the sections from the db, we need to compare them to the mods file sections
            LinkedList<sectionMetadata> notebookSectionsMetadata = notebook.getSections();
            for (sectionMetadata sm: notebookSectionsMetadata) {
                // remove the section_identifier from the sections list if we find it in the current mods file
                if (sections.contains(sm.getIdentifier())) {
                    sections.remove(sm.getIdentifier());
                } else {
                    // since this method is called after adding any new sections to the db, we'll throw an exception
                    // if we find that the xml file contains a section not found in the db
                    throw new ServerErrorException("section exists in xml file: " + notebook.getFilename() + " that was not found in the db");
                }
            }

            if (sections.size() > 0) {
                // the sections list is now a list of sections that are found in the db, but not in the current mods file
                sql = "DELETE FROM section WHERE section_identifier IN (";
                for (String s: sections) {
                    sql += "?, ";
                }
                sql = sql.substring(0, sql.lastIndexOf(","));
                sql += ")";

                stmt = conn.prepareStatement(sql);

                int i = 1;
                for (String s: sections) {
                    stmt.setString(i, s);
                }
                stmt.execute();
            }

        } catch (SQLException e) {
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, rs);
        }
    }

    private void savePage(pageMetadata page, String section_identifier) {
        PreparedStatement stmt = null;
        String sql = null;
        try {
            sql = "INSERT INTO page (section_id, page_number, page_identifier, type) VALUES ((SELECT section_id " +
                "FROM section WHERE section_identifier = ?),?,?,?) " +
                "ON DUPLICATE KEY UPDATE " +
                "section_id = VALUES(section_id), page_number = VALUES(page_number), type = VALUES(type)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, section_identifier);
            stmt.setInt(2, page.getPageNumberAsInt());
            stmt.setString(3, page.getFullPath());
            // TODO insert type
            stmt.setString(4, null);

            stmt.execute();
        } catch (SQLException e) {
            System.out.println(sql + ":" + section_identifier);
            throw new ServerErrorException(e);
        } finally {
            db.close(stmt, null);
        }
    }

    private void saveVolume() {
        PreparedStatement stmt = null;
        String sql = null;
        try {
            sql = "INSERT INTO volume (volume_identifier, type, title, startDate, endDate, family_name, given_name, filename) VALUES (" +
                "?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE " +
                "type = VALUES(type), title = VALUES(title), startDate = VALUES(startDate), endDate = VALUES(endDate), " +
                "family_name = VALUES(family_name), given_name = VALUES(given_name), filename = VALUES(filename)";
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
     * remove any notebooks in the list
     * @param files list of files to be deleted
     */
    public void removeNotebooksInList(List<String> files) {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM volume WHERE filename IN (";

            for (String file : files) {
                sql += "?, ";
            }
            sql = sql.substring(0, sql.lastIndexOf(","));
            sql += ")";

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
     *
     * @param f
     *
     * @return
     *
     * @throws validationException
     */
    private String processFile(File f) throws validationException {
        System.out.println();
        System.out.println("Processing: " + f.toString());

        Mods mods = new modsFactory("file:///" + f.getAbsolutePath()).getMods();

        String[] filepath = f.getAbsolutePath().split("/");
        String filename = filepath[filepath.length - 1];

        try {
            System.out.println();
            System.out.println("Importing: " + f.getAbsolutePath());
            importNotebook(mods);
        } catch (Exception e) {
            System.out.println("ERROR IMPORTING " + e);
            e.printStackTrace();
        }
        return filename;
    }
}
