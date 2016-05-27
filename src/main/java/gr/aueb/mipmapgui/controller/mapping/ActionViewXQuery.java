package gr.aueb.mipmapgui.controller.mapping;

import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.utility.SpicyEngineConstants;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;
import java.util.List;

public class ActionViewXQuery {
    private Modello modello;
    private String scenarioNo;
    
    public ActionViewXQuery(Modello modello, String scenarioNo) {        
        this.modello = modello;
        this.scenarioNo = scenarioNo;
    }
    
    public String performAction() {
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        return getPath(mappingTask);       
    }
    
    private String getPath(MappingTask mappingTask) {
        String path = "";
        if (mappingTask.getSourceProxy().getType().equalsIgnoreCase(SpicyEngineConstants.TYPE_XML)) {
            List<String> sourceInstancesName = (List<String>) mappingTask.getSourceProxy().getAnnotation(SpicyEngineConstants.XML_INSTANCE_FILE_LIST);
            path = sourceInstancesName.get(sourceInstancesName.size() - 1);
            path = path.replace("\\", "/");
        }
        String query = mappingTask.getMappingData().getXQueryScript(path);
        return query;
    }
}
