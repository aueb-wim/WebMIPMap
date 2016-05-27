package gr.aueb.connection;

import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.datasource.JoinCondition;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;


public class ActionDeleteJoinCondition {
    private Modello modello;
    private String scenarioNo;        
        
    public ActionDeleteJoinCondition(Modello model, String scenarioNo) {
        this.modello = model;
        this.scenarioNo=scenarioNo;
    }
    
    public void performAction(String sourcePath, String targetPath, boolean isSource) {
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        IDataSourceProxy dataSource;
        if (isSource){
            dataSource=mappingTask.getSourceProxy();
        }
        else{
            dataSource=mappingTask.getTargetProxy();
        }
        String key = sourcePath+"->"+targetPath;
        JoinCondition jcToDelete = scenario.getJoinCondition(key);
        dataSource.removeJoinCondition(jcToDelete);
        mappingTask.setModified(true);
        scenario.removeJoinCondition(key);     
    }

}
