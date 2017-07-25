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

package gr.aueb.connection;

//giannisk
import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class ActionDeleteConnection {
    private Modello modello;
    private String scenarioNo;        
        
    public ActionDeleteConnection(Modello model,String scenarioNo) {
        this.modello = model;
        this.scenarioNo=scenarioNo;
    }
    
    public void performAction(String sourcePath, String targetPath) {

        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        if(targetPath!=null&&!targetPath.equals("")){
            String key = sourcePath + "->" + targetPath;
            ValueCorrespondence vcToDelete = scenario.getCorrespondence(key);
            mappingTask.removeCorrespondence(vcToDelete);
            ////mappingTask.removeCandidateCorrespondence(vcToDelete);
            scenario.removeCorrespondence(targetPath);
        }
    }
    
    public void performActionManyConnections(String[] sourcePathArray, String[] targetPathArray) { 
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        if (sourcePathArray!=null && targetPathArray!=null && sourcePathArray.length==targetPathArray.length){
            for (int i = 0; i < sourcePathArray.length; i++){
                if(sourcePathArray[i]!=null && targetPathArray[i]!=null && !targetPathArray[i].equals("")){
                    String fromPath = sourcePathArray[i];
                    String toPath = targetPathArray[i];
                    String key = fromPath + "->" + toPath;
                    ValueCorrespondence vcToDelete = scenario.getCorrespondence(key);
                    mappingTask.removeCorrespondence(vcToDelete);
                    scenario.removeCorrespondence(key);
                }
            }
        }
    }

}
