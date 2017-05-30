/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.UserMappingCorrespondences;
import it.unibas.spicy.persistence.DAOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.openide.util.Exceptions;

/**
 *
 * @author ioannisxar
 */
public class ActionGetRecommendedScenario {
    
    private HashMap<String, String> commonScenarios;
    
    public ActionGetRecommendedScenario(HashMap<String, String> commonScenarios){
        this.commonScenarios = commonScenarios;
    }
    
    public void performAction(){
        ArrayList<UserMappingCorrespondences> umc = new ArrayList<>();
        commonScenarios.forEach((user,mappingName)->{
            OpenMappingScenario scenarioToMatch = new OpenMappingScenario(user, mappingName);
            try {
                umc.add(new UserMappingCorrespondences(user, mappingName, scenarioToMatch.getScenarioCorrespondences()));
            } catch (DAOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        GetCorrespondenceScore score = new GetCorrespondenceScore(umc);
        score.performAction();
        
    }
    
}
