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
package gr.aueb.mipmapgui.controller.datasource;

import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import gr.aueb.mipmapgui.controller.datasource.operators.GenerateSchemaTree;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
////import it.unibas.spicygui.vista.MappingTaskTopComponent;


public class ActionViewSchema {

    private Modello modello;
    private JSONObject sourceSchemaTreeData;
    private JSONObject targetSchemaTreeData;
    private JSONObject treeObject = new JSONObject();
    private GenerateSchemaTree treeGenerator = new GenerateSchemaTree();

    public ActionViewSchema(Modello model) {
        this.modello = model;
    }
    
    public void performAction() {
        Scenario scenario = (Scenario) this.modello.getBean(Costanti.CURRENT_SCENARIO);
        if (scenario != null) { 
            MappingTask mappingTask = scenario.getMappingTask();
            if (mappingTask == null) {
                return;
            }
            IDataSourceProxy sourceProxy = mappingTask.getSourceProxy();
            IDataSourceProxy targetProxy = mappingTask.getTargetProxy();
            this.sourceSchemaTreeData = treeGenerator.buildSchemaTreeDataObject(sourceProxy);
            this.targetSchemaTreeData = treeGenerator.buildSchemaTreeDataObject(targetProxy);
            JSONArray treeArr = new JSONArray();
            treeArr.add(sourceSchemaTreeData);
            treeArr.add(targetSchemaTreeData);        
            this.treeObject.put("trees", treeArr);
        }       
    }
    
    public JSONObject getSourceTreeData(){
        return this.sourceSchemaTreeData;
    }
        
    public JSONObject getTargetTreeData(){
        return this.targetSchemaTreeData;
    }
    
    public JSONObject getSchemaTreesObject(){
        return this.treeObject;
    }
    
    public String getName() {
        return Costanti.ACTION_SHOW_SCHEMA;
    }

}
