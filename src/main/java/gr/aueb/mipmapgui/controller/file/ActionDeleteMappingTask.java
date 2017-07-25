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
