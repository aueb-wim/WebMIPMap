package gr.aueb.mipmapgui.controller.file;

//giannisk
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


public class ActionDeleteMappingTask {
    private Modello modello;
    
    public ActionDeleteMappingTask(Modello modello) {
        this.modello=modello;
    }
    
    public void performAction(String deleteName, String user){
        try {
            //String mappingTaskdirectory = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + deleteName;
            String mappingTaskdirectory = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + deleteName;
            File directory = new File(mappingTaskdirectory);
            FileUtils.deleteDirectory(directory);
        } catch (IOException ex) {
            Logger.getLogger(ActionDeleteMappingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
