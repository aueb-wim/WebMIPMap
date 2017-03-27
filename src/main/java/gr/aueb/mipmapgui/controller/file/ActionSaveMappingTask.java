/*
    Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
    Giansalvatore Mecca - giansalvatore.mecca@unibas.it
    Salvatore Raunich - salrau@gmail.com
    Marcello Buoncristiano - marcello.buoncristiano@yahoo.it

    This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool
    
    ++Spicy is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    ++Spicy is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package gr.aueb.mipmapgui.controller.file;

import static gr.aueb.controllers.MappingController.user;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.DAOMappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.FileUtils;

public class ActionSaveMappingTask{

    private static Log logger = LogFactory.getLog(ActionSaveMappingTask.class);
    private Modello modello;
    private int scenarioNo;
    private DAOMappingTask daoMappingTask = new DAOMappingTask();

    public ActionSaveMappingTask(Modello modello, int scenarioNo) {
        this.modello = modello;
        this.scenarioNo = scenarioNo;
    }
    
    public void performAction(String saveName, String user, boolean overwrite, String previousName, boolean fromGlobal, 
            boolean fromTrustedUser, String trustedUser, boolean saveGlobal, boolean savePublic) {
        Scenario scenario = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
        MappingTask  mappingTask = scenario.getMappingTask();               
        try {
            String initialPath;
            String mappingTaskFile;
            if (overwrite) {
                if (fromGlobal) {
                   initialPath = MipmapDirectories.getGlobalPath() + previousName +"/";
                   mappingTaskFile = initialPath + "mapping_task_" + user.hashCode() + ".xml"; 
                }
                else if (fromTrustedUser) {
                    initialPath = MipmapDirectories.getUserPublicPath(trustedUser) + previousName +"/";
                    mappingTaskFile = initialPath + "mapping_task_" + user.hashCode() + ".xml";
                }
                else {
                    initialPath = MipmapDirectories.getUserPrivatePath(user) + previousName +"/";
                    mappingTaskFile = initialPath + "mapping_task_new.xml";
                }
            }
            else {
                initialPath = MipmapDirectories.getUserTempPath(user);
                mappingTaskFile = initialPath + "mapping_task.xml";
            }
            //create a new xml mapping task file and save the mapping task information to it
            File file = new File(mappingTaskFile);
            daoMappingTask.saveMappingTask(mappingTask, file.getAbsolutePath());
            //create a new folder with the specified save name 
            //and copy the source and target folders' contents to it
            copyContents(saveName, user, initialPath, overwrite, saveGlobal, savePublic, file);                       
            mappingTask.setModified(false);           
        } catch (DAOException | IOException ex) {
            logger.error(ex);
        }
    }
    
    private void copyContents(String saveName, String user, String initialPath, boolean overwrite, boolean saveGlobal, 
            boolean savePublic, File initFile) throws IOException{
        File initDirSrc = new File(initialPath + "source");
        File initDirTarget = new File(initialPath + "target");
        String destFolderPath;
        if (saveGlobal) {
            destFolderPath = MipmapDirectories.getGlobalPath() + saveName + "/";
        }
        else if (savePublic) {
            destFolderPath = MipmapDirectories.getUserPublicPath(user) + saveName + "/";
        }
        else {
            destFolderPath = MipmapDirectories.getUserPrivatePath(user) +saveName + "/";
        }
        File destDir = new File(destFolderPath);
        File destDirSrc = new File(destFolderPath + "source");
        File destDirTarget = new File(destFolderPath + "target");
        //copy folders with source and target files
        FileUtils.copyDirectory(initDirSrc, destDirSrc);
        FileUtils.copyDirectory(initDirTarget, destDirTarget);
        if (overwrite) {            
            File destFile = new File(destFolderPath + "mapping_task.xml");
            //move and rename mapping task xml file
            //FileUtils.moveFile(initFile, destFile);
            Files.move(initFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
        }
        else {
            //move mapping task xml file
            FileUtils.copyFileToDirectory(initFile, destDir);
        }
    }

}