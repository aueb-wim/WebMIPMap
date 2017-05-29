/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.users.ActionGetUsers;
import gr.aueb.users.recommendation.mappingmodel.MappingScenario;
import gr.aueb.users.recommendation.mappingmodel.Schema;
import it.unibas.spicy.persistence.DAOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.simple.JSONArray;
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
    
    public HashMap<String, String> findCommonScenarions() throws DAOException, IOException{
        OpenMappingScenario scenarioToMatch = new OpenMappingScenario(user, mappingName);
        Schema sourceSchemaToCheck = scenarioToMatch.getScenarioSchema("source", "private");
        Schema targetSchemaToCheck = scenarioToMatch.getScenarioSchema("target", "private");
        ArrayList<MappingScenario> trustedUserPublicMappings = trustedMappingsToCheck();
        HashMap<String, String> commonScenarios = new HashMap<>();
        for(MappingScenario scenario: trustedUserPublicMappings){
            //checks if both source and target schemata are common in both scenarios
            if(sourceSchemaToCheck.compareSchemata(scenario, "source") && targetSchemaToCheck.compareSchemata(scenario, "target")){
                commonScenarios.put(scenario.getUserName(), scenario.getMappingTaskName());
            }
        }
        return commonScenarios;
    }
    
    private ArrayList<MappingScenario> trustedMappingsToCheck() throws DAOException, IOException{
        ArrayList<MappingScenario> trustedUserPublicMappings = new ArrayList<>();
        ActionGetUsers actionGetUsers = new ActionGetUsers();
        actionGetUsers.performAction(user);
        JSONObject outputObject = actionGetUsers.getJSONObject();
        JSONArray trustedUsers = (JSONArray) outputObject.get("trustUsers");
        Iterator<JSONObject> iterator = trustedUsers.iterator();
        while (iterator.hasNext()) {
            JSONObject innerObject = iterator.next();
            JSONArray publicTasks = (JSONArray) innerObject.get("publicTasks");
            Iterator<JSONObject> publicTasksIterator = publicTasks.iterator();
            while (publicTasksIterator.hasNext()) {
                String userName = (String) innerObject.get("userName");
                String mappingTaskName = (String) publicTasksIterator.next().get("taskName");
                OpenMappingScenario scenario = new OpenMappingScenario(userName, mappingTaskName);
                Schema sourceSchema = scenario.getScenarioSchema("source", "public");
                Schema targetSchema = scenario.getScenarioSchema("target", "public");
                trustedUserPublicMappings.add(new MappingScenario(userName, mappingTaskName, sourceSchema, targetSchema));
            }
        }
        return trustedUserPublicMappings;
    }
    
}
