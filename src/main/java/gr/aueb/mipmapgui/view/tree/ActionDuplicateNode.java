package gr.aueb.mipmapgui.view.tree;

import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.Scenario;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicygui.commons.Modello;
import java.util.HashMap;

public class ActionDuplicateNode {
    private Modello modello;
    private String scenarioNo;
    private IDataSourceProxy dataSource;
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    
    public ActionDuplicateNode(Modello modello, String scenarioNo, boolean isSource) {        
        this.modello = modello;
        this.scenarioNo = scenarioNo;
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        if (isSource)
            this.dataSource = mappingTask.getSourceProxy();
        else
            this.dataSource = mappingTask.getTargetProxy(); 
    }
      
    public void performAction(String path) {
        PathExpression pathExpression = generatePathExpression(path);
        dataSource.addDuplication(pathExpression);
    }
   
    private PathExpression generatePathExpression(String path) {
        return pathGenerator.generatePathFromString(path);
    }
    
}
