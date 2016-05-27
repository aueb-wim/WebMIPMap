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
import it.unibas.spicy.model.mapping.proxies.ConstantDataSourceProxy;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.DAOMappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import it.unibas.spicygui.commons.LastActionBean;
import gr.aueb.mipmapgui.controller.Scenario;
import gr.aueb.mipmapgui.controller.datasource.ActionViewConnections;
import gr.aueb.mipmapgui.controller.datasource.ActionViewJoinConditions;
import gr.aueb.mipmapgui.controller.datasource.ActionViewSchema;
import gr.aueb.mipmapgui.controller.datasource.operators.JSONTreeCreator;
import java.io.File;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.tools.FileObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;


public class ActionOpenMappingTask {

    private static Log logger = LogFactory.getLog(ActionOpenMappingTask.class);
    private Modello modello;
    private int scenarioNo;
    private JSONObject treeObject = new JSONObject();

    public ActionOpenMappingTask(Modello modello, int scenarioNo) {
        this.modello=modello;
        this.scenarioNo=scenarioNo;
    }

    public Scenario openCompositionFile(String fileAbsoluteFile, File file) {
        Scenario scenario = null;
        try {
            FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML", "xml");
            MappingTask mappingTask = null;            
            DAOMappingTask daoMappingTask = new DAOMappingTask();
            if (xmlFilter.accept(file)) {
                mappingTask = daoMappingTask.loadMappingTask(scenarioNo, fileAbsoluteFile, true);
                //
                scenario = gestioneScenario(file, mappingTask, false);
            } else {
                ///throw new Exception
            }
        } catch (Exception ex) {
            logger.error(ex);
            ////Scenarios.releaseNumber();
        }
        return scenario;

    }
    
    private Scenario gestioneScenario(File file, MappingTask mappingTask, boolean TGDSession) {
        Scenario scenario = new Scenario("Open Mapping Task Scenario "+scenarioNo, mappingTask, true, file);
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        scenarioMap.put(scenarioNo, (Scenario) scenario);
        modello.putBean(Costanti.CURRENT_SCENARIO, scenario);  
        scenario.setTGDSession(TGDSession);
        return scenario;
    }
  
    public void performAction(String openName, String user) {
        Scenario scenario = null;
        String mappingTaskFile = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user + "/" + openName +"/mapping_task.xml";
        File file = new File(mappingTaskFile);
        scenario = openCompositionFile(file.getPath(), file);
        
        JSONTreeCreator treeCreator = new JSONTreeCreator(modello);
        this.treeObject = treeCreator.createSchemaTrees();
    }
    
    public JSONObject getSchemaTreesObject(){
        return this.treeObject;
    }

    public String getName() {
        return Costanti.ACTION_OPEN;
    }

}
