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

import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.DAOMappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import it.unibas.spicygui.commons.LastActionBean;
import gr.aueb.mipmapgui.controller.Scenario;
import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.FileUtils;

public class ActionSaveMappingTask{

    private static Log logger = LogFactory.getLog(ActionSaveMappingTask.class);
    private Modello modello;
    private int scenarioNo;
    private LastActionBean lastActionBean;
    private boolean esito;
    private DAOMappingTask daoMappingTask = new DAOMappingTask();

    public ActionSaveMappingTask(Modello modello, int scenarioNo) {
        this.modello = modello;
        this.scenarioNo = scenarioNo;
        //lastActionBean = (LastActionBean) modello.getBean(Costanti.LAST_ACTION_BEAN);
    }

   /* private void chiediConferma(File file, MappingTask mappingTask) throws DAOException {
        NotifyDescriptor notifyDescriptor = new NotifyDescriptor.Confirmation(NbBundle.getMessage(Costanti.class, Costanti.FILE_EXISTS), DialogDescriptor.YES_NO_OPTION);
        DialogDisplayer.getDefault().notify(notifyDescriptor);
        if (notifyDescriptor.getValue().equals(NotifyDescriptor.YES_OPTION)) {
            daoMappingTask.saveMappingTask(mappingTask, file.getAbsolutePath());
            mappingTask.setModified(false);
            mappingTask.setToBeSaved(false);
            continua = false;
            esito = true;
            Scenario scenario = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
            scenario.setSaveFile(file);
        }
    }*/

    public boolean isEsito() {
        return esito;
    }

    public void performAction(String saveName, String user) {
        Scenario scenario = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
        MappingTask  mappingTask = scenario.getMappingTask();               
        try {            
            //create a new xml mapping task file and save the mapping task information to it
            String mappingTaskFile = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER +"mapping_task.xml";
            File file = new File(mappingTaskFile);
            daoMappingTask.saveMappingTask(mappingTask,file.getAbsolutePath());            
            //create a new folder with the specified save name 
            //and copy the source and target folders' contents to it
            copyTempContents(saveName, user);
            mappingTask.setModified(false);
            //mappingTask.setToBeSaved(false);            
            esito = true;
        } catch (DAOException ex) {
            logger.error(ex);
            esito = false;
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
    
    private void copyTempContents(String saveName, String user) throws IOException{
        String tempFolderPath = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + Costanti.SERVER_TEMP_FOLDER;
        File srcDir = new File(tempFolderPath);
        String destFolderPath = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + saveName;
        File destDir = new File(destFolderPath);  
        FileUtils.copyDirectory(srcDir, destDir);
    }

    public String getName() {
        return Costanti.ACTION_SAVE;
    }
}