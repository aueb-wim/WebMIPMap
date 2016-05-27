package gr.aueb.mipmapgui.controller.datasource.operators;

//giannisk

import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.datasource.ActionViewConnections;
import gr.aueb.mipmapgui.controller.datasource.ActionViewDuplications;
import gr.aueb.mipmapgui.controller.datasource.ActionViewJoinConditions;
import gr.aueb.mipmapgui.controller.datasource.ActionViewSchema;
import gr.aueb.mipmapgui.controller.datasource.ActionViewSelectionConditions;
import org.json.simple.JSONObject;

public class JSONTreeCreator {
    
    private Modello modello;
    
    
    public JSONTreeCreator(Modello modello) {
        this.modello=modello;
    }
    
    public JSONObject createSchemaTrees(){
        
        ActionViewSchema actionViewSchema = new ActionViewSchema(modello);
        actionViewSchema.performAction();        
        JSONObject treeObject = actionViewSchema.getSchemaTreesObject();
        
        ActionViewJoinConditions actionViewJoinConditions = new ActionViewJoinConditions(modello);
        actionViewJoinConditions.performAction();        
        JSONObject joinsObject = actionViewJoinConditions.getJoinConditionsObject();
        treeObject.putAll(joinsObject);
        
        ActionViewConnections actionViewConnections = new ActionViewConnections(modello);
        actionViewConnections.performAction();        
        JSONObject connectionsObject = actionViewConnections.getConnectionsObject();
        treeObject.putAll(connectionsObject);
        
        ActionViewSelectionConditions actionViewSelectionConditions = new ActionViewSelectionConditions(modello);
        actionViewSelectionConditions.performAction();        
        JSONObject selConditionsObject = actionViewSelectionConditions.getSelConditionsObject();
        treeObject.putAll(selConditionsObject);
        
        ActionViewDuplications actionViewDuplications = new ActionViewDuplications(modello);
        actionViewDuplications.performAction();        
        JSONObject duplicationsObject = actionViewDuplications.getDuplicationsObject();
        treeObject.putAll(duplicationsObject);
        
        return(treeObject);
    }
}
