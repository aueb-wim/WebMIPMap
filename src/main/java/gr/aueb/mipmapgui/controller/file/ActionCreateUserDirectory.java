package gr.aueb.mipmapgui.controller.file;

import gr.aueb.mipmapgui.Costanti;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ActionCreateUserDirectory {
    
    private List<Path> getUserDirectories(String user) {
        List<Path> userDirectories = new ArrayList<>();
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_SCHEMATA_FOLDER)); //is it safe to check every time?
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER)); //is it safe to check every time?
        //user folder
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user));
        //user sub-folders
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_PRIVATE_USER_FOLDER));
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_PUBLIC_USER_FOLDER));
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER));
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER 
                + Costanti.SERVER_TEMP_TASKS_FOLDER));
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER 
                + Costanti.SERVER_TEMP_TASKS_FOLDER + Costanti.SERVER_SOURCE_FOLDER));
        userDirectories.add(Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER 
                + Costanti.SERVER_TEMP_TASKS_FOLDER + Costanti.SERVER_TARGET_FOLDER));
        return userDirectories;
    };
    
    public ActionCreateUserDirectory() {}    
    
    public void performAction(String user) {          
        for (Path path : getUserDirectories(user)) {
            if (!Files.exists(path)) {
                new File(path.toString()).mkdir();
            }
        }
    }   
}
