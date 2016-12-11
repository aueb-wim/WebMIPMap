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
