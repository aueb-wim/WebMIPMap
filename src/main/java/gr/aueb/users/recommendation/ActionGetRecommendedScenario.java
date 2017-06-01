/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.UserMappingCorrespondences;
import it.unibas.spicy.persistence.DAOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Exceptions;

/**
 *
 * @author ioannisxar
 */
public class ActionGetRecommendedScenario {
    
    private HashMap<String, String> commonScenarios;
    private OpenMappingScenario scenario;
    public ActionGetRecommendedScenario(HashMap<String, String> commonScenarios){
        this.commonScenarios = commonScenarios;
    }
    
    public void performAction() throws DAOException, IOException{
        ArrayList<UserMappingCorrespondences> umc = new ArrayList<>();
        commonScenarios.forEach((user,mappingName)->{
            OpenMappingScenario scenarioToMatch = new OpenMappingScenario(user, mappingName);
            this.scenario = scenarioToMatch;
            try {
                umc.add(new UserMappingCorrespondences(user, mappingName, scenarioToMatch.getScenarioCorrespondences()));
            } catch (DAOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        GetRecommendedCorrespondences score = new GetRecommendedCorrespondences(umc, this.scenario);
        Map<String, Correspondence> recommendedCorrespondencesPerTarget = score.performAction();
        generateMappingTask(recommendedCorrespondencesPerTarget, this.scenario);
    }
    
    private void generateMappingTask(Map<String, Correspondence> correspondences, OpenMappingScenario scenario){
        try {
            scenario.getScenarioSchema("source", "public").printSchema();
            scenario.getScenarioSchema("target", "public").printSchema();
        } catch (DAOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
}
