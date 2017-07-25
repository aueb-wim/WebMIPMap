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
