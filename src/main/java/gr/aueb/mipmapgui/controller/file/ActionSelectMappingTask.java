package gr.aueb.mipmapgui.controller.file;

//giannisk

import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;

public class ActionSelectMappingTask {
    private Modello modello;    
    
    public ActionSelectMappingTask(Modello modello) {
        this.modello=modello;
    }
    
    public void performAction(String scenarioNo){
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenarioNew = scenarioMap.get(Integer.valueOf(scenarioNo));
        modello.putBean(Costanti.CURRENT_SCENARIO, scenarioNew);
    }
    
}
