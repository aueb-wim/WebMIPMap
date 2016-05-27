package gr.aueb.mipmapgui.controller.file;

import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import java.io.File;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ActionInitialize {
     private Modello modello;
     private JSONObject JSONObject = new JSONObject();
    
    public ActionInitialize(Modello modello) {
        this.modello=modello;
    }
    
    public void performAction(String user) {
        getSavedUserFiles(user);
        getSavedSchemata();        
    }
    
    private void getSavedUserFiles(String user) {        
        JSONArray taskFileArr = new JSONArray();
        
        ActionCreateUserDirectory actionCreateUserDirectory = new ActionCreateUserDirectory(modello);
        actionCreateUserDirectory.performAction(user);
        ActionCleanDirectory actionCleanDirectory = new ActionCleanDirectory(modello);
        actionCleanDirectory.performAction(user);

        File tasksDir = new File(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/");
        String[] taskFiles = tasksDir.list(DirectoryFileFilter.INSTANCE);
        for (String file : taskFiles) {
            if (!file.equalsIgnoreCase("temp")){
                taskFileArr.add(file);
            }
        }                    
        JSONObject.put("savedTasks", taskFileArr);
    }
    
    private void getSavedSchemata(){
        JSONArray schemaFileArr = new JSONArray();
        File schemaDir = new File(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_SCHEMATA_FOLDER);
        String[] schemaFiles = schemaDir.list();
        for (String file : schemaFiles) {
            file = file.substring(0, file.lastIndexOf('.'));
            schemaFileArr.add(file);
        }
        JSONObject.put("savedSchemata", schemaFileArr);
    }
    
     public JSONObject getJSONObject(){
        return this.JSONObject;
    }
    
}
