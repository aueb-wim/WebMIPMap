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

//giannisk

import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.operators.FindNode;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import it.unibas.spicy.model.correspondence.GetIdFromDb;
import it.unibas.spicy.utility.SpicyEngineConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ActionViewConnections {
        
    private Modello modello;
    private JSONObject connectionsObject = new JSONObject();
    
    public ActionViewConnections(Modello model) {
        this.modello = model;
    }
    
    public void performAction() {
        Scenario scenario = (Scenario) this.modello.getBean(Costanti.CURRENT_SCENARIO);
        if (scenario != null) {
            JSONArray connectionsArray = new JSONArray();

            FindNode finder = new FindNode();

            MappingTask mappingTask = scenario.getMappingTask();
            if (mappingTask.getValueCorrespondences().size() > 0) {
                 for (ValueCorrespondence valueCorrespondence: mappingTask.getValueCorrespondences()){
                     JSONObject connectionObject = new JSONObject();
                     String fromPath ="";
                     //constant
                     if (valueCorrespondence.getSourceValue() != null) {
                         connectionObject.put("connectionType","constant");
                         connectionObject.put("sourceValue",valueCorrespondence.getSourceValue().toString());
                         connectionObject.put("constantType", valueCorrespondence.getSourceValue().getType());
                         String sequenceName = valueCorrespondence.getSourceValue().getSequence();
                         connectionObject.put("sequence", sequenceName);
                         connectionObject.put("offset", SpicyEngineConstants.OFFSET_MAPPING.get(sequenceName));
                         GetIdFromDb objDb = SpicyEngineConstants.GET_ID_FROM_DB.get(sequenceName);
                         if(objDb != null){
                            connectionObject.put("dbDriver", objDb.getDriver());
                            connectionObject.put("dbUri", objDb.getUri());
                            connectionObject.put("dbSchema", objDb.getSchema());
                            connectionObject.put("dbUsername", objDb.getLogin());
                            connectionObject.put("dbPassword", objDb.getPassword());
                            connectionObject.put("dbTable", objDb.getTable());
                            connectionObject.put("dbColumn", objDb.getColumn());
                            connectionObject.put("dbFunction", objDb.getFunction());
                         }
                         connectionObject.put("transformationFunction",null);
                         connectionObject.put("sourceNode",null);
                         INode iNodeTarget = finder.findNodeInSchema(valueCorrespondence.getTargetPath(), mappingTask.getTargetProxy());
                         connectionObject.put("targetNode","sch_node" + iNodeTarget.getValue());
                         fromPath = valueCorrespondence.getSourceValue().toString();
                     }
                     else{
                         //function
                         if (verificaFunzioneDiTrasformazione(valueCorrespondence)) {
                             connectionObject.put("connectionType","function");
                             connectionObject.put("sourceValue",null);
                             connectionObject.put("transformationFunction",valueCorrespondence.getTransformationFunction().toString());
                             JSONArray sourcePathsArray = new JSONArray();
                             for (PathExpression sourcePath : valueCorrespondence.getSourcePaths()){
                                 INode iNodeSource = finder.findNodeInSchema(sourcePath, mappingTask.getSourceProxy());
                                 sourcePathsArray.add("sch_node" + iNodeSource.getValue());
                             }
                             connectionObject.put("sourceNodes",sourcePathsArray);
                             INode iNodeTarget = finder.findNodeInSchema(valueCorrespondence.getTargetPath(), mappingTask.getTargetProxy());
                             connectionObject.put("targetNode","sch_node" + iNodeTarget.getValue());
                             fromPath = valueCorrespondence.getTransformationFunction().toString();
                         }
                         //simple 1:1 correspondence
                         else{
                             connectionObject.put("connectionType","simple");
                             connectionObject.put("sourceValue",null);
                             connectionObject.put("transformationFunction",null);
                             JSONArray sourcePathsArray = new JSONArray();
                             for (PathExpression sourcePath : valueCorrespondence.getSourcePaths()){
                                 INode iNodeSource = finder.findNodeInSchema(sourcePath, mappingTask.getSourceProxy());
                                 sourcePathsArray.add("sch_node" + iNodeSource.getValue());
                             }
                             connectionObject.put("sourceNodes",sourcePathsArray);
                             INode iNodeTarget = finder.findNodeInSchema(valueCorrespondence.getTargetPath(), mappingTask.getTargetProxy());                                        
                             connectionObject.put("targetNode","sch_node" + iNodeTarget.getValue());
                             fromPath = valueCorrespondence.getSourcePaths().get(0).toString();
                         }
                     }
                     connectionsArray.add(connectionObject);
                     
                     String toPath = valueCorrespondence.getTargetPath().toString();
                     String key = fromPath + "->" + toPath;
                     
                     scenario.addCorrespondence(key, valueCorrespondence);
                 }                           
            }
            connectionsObject.put("connections",connectionsArray);
        }                 
    }
   
    private boolean verificaFunzioneDiTrasformazione(ValueCorrespondence valueCorrespondence) {
        if (valueCorrespondence.getSourcePaths().size() > 1) {
            return true;
        }
        String transformationFunctionString = valueCorrespondence.getTransformationFunction().toString();
        String sourcePathString = valueCorrespondence.getSourcePaths().get(0).toString();
        return (!transformationFunctionString.equals(sourcePathString));
    }
    
    public JSONObject getConnectionsObject(){
        return this.connectionsObject;
    }
}
