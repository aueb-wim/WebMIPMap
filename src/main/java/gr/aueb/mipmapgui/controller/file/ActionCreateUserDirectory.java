package gr.aueb.mipmapgui.controller.file;

import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ActionCreateUserDirectory {
    private Modello modello;
    
    public ActionCreateUserDirectory(Modello modello) {
        this.modello=modello;
    }    
    
    public void performAction(String user) {       
            Path path_upload = Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER);
            if (!Files.exists(path_upload))
                new File(path_upload.toString()).mkdir();

            Path path_user = Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user);
            if (!Files.exists(path_user)){
                //create user folder
                new File(path_user.toString()).mkdir();
                //create user sub-folders
                new File(path_user + "/" + Costanti.SERVER_TEMP_FOLDER).mkdir();
                new File(path_user + "/" + Costanti.SERVER_TEMP_FOLDER+ "/" + Costanti.SERVER_SOURCE_FOLDER).mkdir();
                new File(path_user + "/" + Costanti.SERVER_TEMP_FOLDER+ "/" + Costanti.SERVER_TARGET_FOLDER).mkdir();
            } 

            Path path_schemata = Paths.get(Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_SCHEMATA_FOLDER);
            if (!Files.exists(path_schemata))
                new File(path_schemata.toString()).mkdir();

    }   
}
