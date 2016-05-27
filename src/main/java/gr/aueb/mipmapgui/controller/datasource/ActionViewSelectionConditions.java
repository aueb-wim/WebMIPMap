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
