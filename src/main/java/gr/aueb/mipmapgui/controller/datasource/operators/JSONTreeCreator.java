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
