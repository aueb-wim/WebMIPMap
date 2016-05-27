package gr.aueb.mipmapgui.controller.datasource;

//giannisk

import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.JoinCondition;
import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ActionViewJoinConditions {
        
    private Modello modello;
    private JSONObject joinsObject = new JSONObject();
    
    public ActionViewJoinConditions(Modello model) {
        this.modello = model;
    }
    
    public void performAction() {
        Scenario scenario = (Scenario) this.modello.getBean(Costanti.CURRENT_SCENARIO);
        if (scenario != null) {
            JSONArray joinsArray = new JSONArray();

            FindNode finder = new FindNode();

            MappingTask mappingTask = scenario.getMappingTask();
            IDataSourceProxy sourceProxy = mappingTask.getSourceProxy();
            IDataSourceProxy targetProxy = mappingTask.getTargetProxy();
            
            if ((sourceProxy.getJoinConditions().size() + targetProxy.getJoinConditions().size()) > 0) {          
                for (JoinCondition sourceJoin: sourceProxy.getJoinConditions()){
                   JSONObject joinObject = new JSONObject();                    
                   INode iNodeSource = finder.findNodeInSchema(sourceJoin.getFromPaths().get(0), sourceProxy);                                        
                   joinObject.put("sourceNode","sch_node" + iNodeSource.getValue());
                   INode iNodeTarget = finder.findNodeInSchema(sourceJoin.getToPaths().get(0), sourceProxy);                                        
                   joinObject.put("targetNode","sch_node" + iNodeTarget.getValue());
                   joinObject.put("mandatory",sourceJoin.isMandatory());
                   joinObject.put("fk",sourceJoin.isMonodirectional());
                   joinsArray.add(joinObject);
                
                   String key = sourceJoin.getFromPaths().get(0)+"->"+sourceJoin.getToPaths().get(0);
                   scenario.addJoinCondition(key, sourceJoin); 
                }
                for (JoinCondition targetJoin: targetProxy.getJoinConditions()){
                   JSONObject joinObject = new JSONObject();                    
                   INode iNodeSource = finder.findNodeInSchema(targetJoin.getFromPaths().get(0), targetProxy);                                        
                   joinObject.put("sourceNode","sch_node" + iNodeSource.getValue());
                   INode iNodeTarget = finder.findNodeInSchema(targetJoin.getToPaths().get(0), targetProxy);                                        
                   joinObject.put("targetNode","sch_node" + iNodeTarget.getValue());
                   joinObject.put("mandatory",targetJoin.isMandatory());
                   joinObject.put("fk",targetJoin.isMonodirectional());
                   joinsArray.add(joinObject);
                   
                   String key = targetJoin.getFromPaths().get(0)+"->"+targetJoin.getToPaths().get(0);
                   scenario.addJoinCondition(key, targetJoin); 
                }
            }
            joinsObject.put("joins",joinsArray);
        }                 
    }   
    
    public JSONObject getJoinConditionsObject(){
        return this.joinsObject;
    }
}
