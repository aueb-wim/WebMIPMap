/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import static gr.aueb.controllers.MappingController.user;
import gr.aueb.users.ActionGetUsers;
import gr.aueb.users.recommendation.mappingmodel.Field;
import gr.aueb.users.recommendation.mappingmodel.Schema;
import gr.aueb.users.recommendation.mappingmodel.Table;
import it.unibas.spicy.persistence.DAOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author ioannisxar
 */
public class ActionFindCommonMappingTasks {
    
    private String user, mappingName;
    
    public ActionFindCommonMappingTasks(String user, String mappingName){
        this.user = user;
        this.mappingName = mappingName;
    }
    
    public void findCommonScenarions() throws DAOException, IOException{
        OpenMappingScenario scenarioToMatch = new OpenMappingScenario(user, mappingName);
        Schema sourceSchema = scenarioToMatch.getScenarioSchema("source", "private");
        Schema targetSchema = scenarioToMatch.getScenarioSchema("target", "private");
        
        ActionGetUsers actionGetUsers = new ActionGetUsers();
        actionGetUsers.performAction(user);
        JSONObject outputObject = actionGetUsers.getJSONObject();
        System.out.println(outputObject.toJSONString());
    }
    
    private void printSchema(Schema schema){
        System.out.println("Database Name: " + schema.getDatabaseName());
        for(Table t: schema.getDatabaseTables()){
            HashMap<String, ArrayList<Field>> table = t.getTableName();
            for (Map.Entry<String, ArrayList<Field>> entry : table.entrySet()) {
                System.out.println("Table Name: " + entry.getKey());
                for(Field f : entry.getValue()){
                    System.out.println("Attribute Name: " + f.getFieldName());
                    System.out.println("Attribute Type: " + f.getFieldType());
                }
            }
        }
    }
}
