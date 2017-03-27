package gr.aueb.mipmapgui.controller.file;

//giannisk
import static gr.aueb.controllers.MappingController.user;
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
    
    public void performAction(String deleteName, String user, boolean isPublic){
        try {
            String mappingTaskdirectory;
            if (isPublic) {
                mappingTaskdirectory = MipmapDirectories.getUserPublicPath(user) + deleteName;
            } else {
                mappingTaskdirectory = MipmapDirectories.getUserPrivatePath(user) + deleteName;
            }
            File directory = new File(mappingTaskdirectory);
            FileUtils.deleteDirectory(directory);
        } catch (IOException ex) {
            Logger.getLogger(ActionDeleteMappingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
