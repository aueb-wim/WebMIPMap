package gr.aueb.mipmapgui.controller.mapping;

import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;

public class ActionViewSql {
    private Modello modello;
    private int scenarioNo;
    
    public ActionViewSql(Modello modello, int scenarioNo) {        
        this.modello = modello;
        this.scenarioNo = scenarioNo;
    }
    
    public String performAction() {
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(scenarioNo);
        MappingTask mappingTask = scenario.getMappingTask();
        return mappingTask.getMappingData().getSQLScript(scenarioNo);
    }
}
