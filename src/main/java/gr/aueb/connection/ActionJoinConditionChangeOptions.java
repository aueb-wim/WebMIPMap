package gr.aueb.connection;

//giannisk

import it.unibas.spicy.model.datasource.JoinCondition;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;

public class ActionJoinConditionChangeOptions {
    
    private Modello modello;
    private String scenarioNo;
    
    public ActionJoinConditionChangeOptions(Modello model,String scenarioNo) {
        this.modello = model;
        this.scenarioNo=scenarioNo;
    }
    
    public void performAction(String changedOption, String sourcePath, String targetPath, boolean isSource){
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
        JoinCondition jcToChange = scenario.getJoinCondition(key);
        switch(changedOption){
            case "mandatory":
                dataSource.setMandatoryForJoin(jcToChange, !jcToChange.isMandatory());                
                mappingTask.setModified(true);
                break;
            case "fk":
                dataSource.setForeignKeyForJoin(jcToChange, !jcToChange.isMonodirectional());
                mappingTask.setModified(true);
                break;
            default:
                break;
        }
    }
}
