package rest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import renderer.sqlImporter;
import utils.SettingsManager;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * REST service for importing, updating, and removing notebooks from the db.
 */
@Path("importer")
public class importer {

    /**
     * Service to point a github push webhook to in order to import any new/modified mods files.
     * See https://developer.github.com/webhooks/ to setup a webhook for your repo.
     */
    @POST
    public void importer(String payload) {
        SettingsManager sm = SettingsManager.getInstance();
        sm.loadProperties();
        String mods_dir = sm.retrieveValue("mods_dir");
        String repo_url = sm.retrieveValue("repo_url");

        sqlImporter sqlImporter = new sqlImporter();

        JSONObject json = (JSONObject) JSONValue.parse(payload);
        JSONArray commits = (JSONArray) json.get("commits");

        // there may be multiple commits so update the mods files for each commit individually
        for (Object c : commits) {
            JSONObject commit = (JSONObject) c;
            List<String> added = new ArrayList<String>();
            List<String> removed = new ArrayList<String>();
            List<String> modified = new ArrayList<String>();
            String sha = (String) commit.get("id");

            for (Object f : (JSONArray) commit.get("added")) {
                String file = (String) f;

                if (file.startsWith(mods_dir)) {
                    added.add(repo_url + sha + file);
                }
            }

            for (Object f : (JSONArray) commit.get("modified")) {
                String file = (String) f;

                if (file.startsWith(mods_dir)) {
                    modified.add(repo_url + sha + file);
                }
            }

            for (Object f : (JSONArray) commit.get("removed")) {
                String file = (String) f;

                if (file.startsWith(mods_dir)) {
                    removed.add(repo_url + sha + file);
                }
            }

            // import, update, and remove the appropriate notebooks
            sqlImporter.importNotebooks(added);
            sqlImporter.updateNotebooks(modified);
            sqlImporter.removeNotebooks(removed);
        }

        return;
    }
}
