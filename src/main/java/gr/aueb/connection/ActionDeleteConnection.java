package gr.aueb.connection;

//giannisk
import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class ActionDeleteConnection {
    private Modello modello;
    private String scenarioNo;        
        
    public ActionDeleteConnection(Modello model,String scenarioNo) {
        this.modello = model;
        this.scenarioNo=scenarioNo;
    }
    
    public void performAction(String sourcePath, String targetPath) {

        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        if(targetPath!=null&&!targetPath.equals("")){
            String key = sourcePath + "->" + targetPath;
            ValueCorrespondence vcToDelete = scenario.getCorrespondence(key);
            mappingTask.removeCorrespondence(vcToDelete);
            ////mappingTask.removeCandidateCorrespondence(vcToDelete);
            scenario.removeCorrespondence(targetPath);
        }
    }
    
    public void performActionAllConnections(String[] sourcePathArray, String[] targetPathArray) { 
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        if (sourcePathArray!=null && targetPathArray!=null && sourcePathArray.length==targetPathArray.length){
            for (int i = 0; i < sourcePathArray.length; i++){
                if(sourcePathArray[i]!=null&&targetPathArray[i]!=null&&!targetPathArray[i].equals("")){
                    String fromPath = sourcePathArray[i];
                    String toPath = targetPathArray[i];
                    String key = fromPath + "->" + toPath;
                    ValueCorrespondence vcToDelete = scenario.getCorrespondence(key);
                    mappingTask.removeCorrespondence(vcToDelete);
                    scenario.removeCorrespondence(key);
                }
            }
        }
    }

}
