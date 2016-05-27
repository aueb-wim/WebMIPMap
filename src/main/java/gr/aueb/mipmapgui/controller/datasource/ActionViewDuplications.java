/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.mipmapgui.controller.datasource;

import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.Scenario;
import it.unibas.spicy.model.datasource.Duplication;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicygui.commons.Modello;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ActionViewDuplications {
    private Modello modello;
    private JSONObject duplicationsObject = new JSONObject();
    
    public ActionViewDuplications(Modello model) {
        this.modello = model;
    }
    
    public void performAction() {
            Scenario scenario = (Scenario) this.modello.getBean(Costanti.CURRENT_SCENARIO);
            if (scenario != null) {
                JSONArray sourceDuplicationsArray = new JSONArray();
                JSONArray targetDuplicationsArray = new JSONArray();
                FindNode finder = new FindNode();
                
                MappingTask mappingTask = scenario.getMappingTask();
                IDataSourceProxy sourceProxy = mappingTask.getSourceProxy();
                IDataSourceProxy targetProxy = mappingTask.getTargetProxy();
                if (sourceProxy.getDuplications().size() + targetProxy.getDuplications().size() > 0) {                    
                    for (Duplication duplication: sourceProxy.getDuplications()){  
                        JSONObject joinObject = new JSONObject();
                        INode iNodeSource = finder.findNodeInSchema(duplication.getClonePath(), sourceProxy); 
                        joinObject.put("cloneNode","sch_node" + iNodeSource.getValue());
                        INode iNodeSource2 = finder.findNodeInSchema(duplication.getOriginalPath(), sourceProxy);
                        joinObject.put("originalNode","sch_node" + iNodeSource2.getValue());
                        sourceDuplicationsArray.add(joinObject);
                    }                    
                    for (Duplication duplication: targetProxy.getDuplications()){  
                        JSONObject joinObject = new JSONObject();
                        INode iNodeSource = finder.findNodeInSchema(duplication.getClonePath(), targetProxy);                                        
                        joinObject.put("cloneNode","sch_node" + iNodeSource.getValue());
                        targetDuplicationsArray.add(joinObject);
                    }
                }
                duplicationsObject.put("sourceDuplications",sourceDuplicationsArray);
                duplicationsObject.put("targetDuplications",targetDuplicationsArray);
            }
    }
    
    public JSONObject getDuplicationsObject(){
        return this.duplicationsObject;
    }
       
}
