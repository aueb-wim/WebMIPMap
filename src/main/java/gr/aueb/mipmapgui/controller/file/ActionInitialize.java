/* Copyright 2015-2016 by the Athens University of Economics and Business (AUEB).
   This file is part of WebMIPMap.
   WebMIPMap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    WebMIPMap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with WebMIPMap.  If not, see <http://www.gnu.org/licenses/>.
 */

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
