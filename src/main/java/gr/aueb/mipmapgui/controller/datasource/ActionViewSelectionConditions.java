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

package gr.aueb.mipmapgui.controller.datasource;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.SelectionCondition;
import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import gr.aueb.mipmapgui.view.tree.ActionSelectionCondition;
import gr.aueb.mipmapgui.view.tree.SelectionConditionInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ActionViewSelectionConditions {
    private Modello modello;
    private JSONObject selConditionsObject = new JSONObject();
    
    public ActionViewSelectionConditions(Modello model) {
        this.modello = model;
    }
    
    public void performAction() {
        Scenario scenario = (Scenario) this.modello.getBean(Costanti.CURRENT_SCENARIO);
        if (scenario != null) {
            JSONArray selConditionsArray = new JSONArray();

            FindNode finder = new FindNode();

            MappingTask mappingTask = scenario.getMappingTask();
            IDataSourceProxy sourceProxy = mappingTask.getSourceProxy();
            if (sourceProxy.getSelectionConditions().size() > 0) {          
                for (SelectionCondition selCondition: sourceProxy.getSelectionConditions()){                   
                   JSONObject joinObject = new JSONObject();                    
                   INode iNodeSource = finder.findNodeInSchema(selCondition.getSetPaths().get(0), sourceProxy);                                        
                   joinObject.put("sourceNode","sch_node" + iNodeSource.getValue());
                   String condition = selCondition.getCondition().toString();
                   joinObject.put("condition", condition);
                   selConditionsArray.add(joinObject);
                   
                   ActionSelectionCondition editSelectionCondition = new ActionSelectionCondition(modello, scenario);
                   SelectionConditionInfo selectionConditionInfo = editSelectionCondition.getSelectionCondition(iNodeSource);
                   selectionConditionInfo.setSelectionCondition(selCondition);
                }
            }
            selConditionsObject.put("selConditions",selConditionsArray);
        }                 
    } 
        
    public JSONObject getSelConditionsObject(){
        return this.selConditionsObject;
    }   
}
