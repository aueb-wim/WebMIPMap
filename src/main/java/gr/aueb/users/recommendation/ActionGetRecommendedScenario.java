/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import it.unibas.spicy.persistence.DAOException;
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
        commonScenarios.forEach((user,mappingName)->{
            OpenMappingScenario scenarioToMatch = new OpenMappingScenario(user, mappingName);
            try {
                scenarioToMatch.getScenarioCorrespondences();
            } catch (DAOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        
    }
    
}
