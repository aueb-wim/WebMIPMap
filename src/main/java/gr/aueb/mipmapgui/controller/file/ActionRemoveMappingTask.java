package gr.aueb.mipmapgui.controller.file;

import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;

//giannisk
public class ActionRemoveMappingTask {
        private Modello modello;    
    
    public ActionRemoveMappingTask(Modello modello) {
        this.modello=modello;
    }
    
    public void performAction(String scenarioNo){
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        //set current scenario as null if the current scenario is the one being removed
        if (scenarioMap.get(Integer.valueOf(scenarioNo)).equals(modello.getBean(Costanti.CURRENT_SCENARIO))){
            modello.putBean(Costanti.CURRENT_SCENARIO, null);
            //modello.putBean(Costanti.CURRENT_SCENARIO, null);
        }
        //delete the scenario that is removed from the mapper
        scenarioMap.put(Integer.valueOf(scenarioNo),null);
    }
}
