package gr.aueb.mipmapgui.view.tree;

import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.datasource.SelectionCondition;
import it.unibas.spicy.model.exceptions.ExpressionSyntaxException;
import it.unibas.spicy.model.expressions.Expression;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;


public class ActionSelectionCondition {
    private Modello modello;
    private String scenarioNo;
    private IDataSourceProxy dataSource;
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    
    public ActionSelectionCondition(Modello modello, String scenarioNo) {        
        this.modello = modello;
        this.scenarioNo = scenarioNo;
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        this.dataSource = mappingTask.getSourceProxy();
    }
    
    public ActionSelectionCondition(Modello modello, Scenario scenario) {        
        this.modello = modello;
        MappingTask mappingTask = scenario.getMappingTask();
        this.dataSource = mappingTask.getSourceProxy();
    }
    
    public void performAction(String action, String path, String expression) {
        INode iNode = getNodeSelected(path);
        SelectionConditionInfo selectionConditionInfo = getSelectionCondition(iNode);
        //replace quotes with single quotes
        expression= expression.replace("'","\"");
        //replace space special characters
        expression= expression.replace("\u00A0","");
        selectionConditionInfo.setExpressionString(expression);
        if (action.equals("update")){
            updateSelectionCondition(path, selectionConditionInfo);
        } else {
            deleteSelectionCondition(selectionConditionInfo);
        }
    }
    
    private void updateSelectionCondition(String path, SelectionConditionInfo selectionConditionInfo){
        try { 
            if (selectionConditionInfo.getSelectionCondition()!=null){
                dataSource.removeSelectionCondition(selectionConditionInfo.getSelectionCondition());
            }
            PathExpression pathExpression = generatePathExpression(path);
            Expression expression = new Expression(selectionConditionInfo.getExpressionString());
            SelectionCondition selectionCondition = new SelectionCondition(pathExpression, expression, dataSource.getIntermediateSchema());
            dataSource.addSelectionCondition(selectionCondition);
            selectionConditionInfo.setSelectionCondition(selectionCondition);
        } catch (ExpressionSyntaxException e) {
            dataSource.addSelectionCondition(selectionConditionInfo.getSelectionCondition());
        }
    }
    
    private void deleteSelectionCondition(SelectionConditionInfo selectionConditionInfo){
        try {
            dataSource.removeSelectionCondition(selectionConditionInfo.getSelectionCondition());
            selectionConditionInfo.setSelectionCondition(null);
            selectionConditionInfo.setExpressionString("");
        } catch (ExpressionSyntaxException e) {
            dataSource.addSelectionCondition(selectionConditionInfo.getSelectionCondition());
        }  
    }
    
    private INode getNodeSelected(String path) {
        PathExpression treePath = generatePathExpression(path);
        INode treeNode = treePath.getLastNode(dataSource.getIntermediateSchema());
        return treeNode;
    }
    
    public SelectionConditionInfo getSelectionCondition(INode iNode) {
        SelectionConditionInfo selectionConditionInfo = null;
        if (iNode.getAnnotation(Costanti.SELECTION_CONDITON_INFO) != null) {
            selectionConditionInfo = (SelectionConditionInfo) iNode.getAnnotation(Costanti.SELECTION_CONDITON_INFO);
        } else {
            selectionConditionInfo = new SelectionConditionInfo();
            iNode.addAnnotation(Costanti.SELECTION_CONDITON_INFO, selectionConditionInfo);
        }
        return selectionConditionInfo;
    }
   
    
    private PathExpression generatePathExpression(String path) {
        return pathGenerator.generatePathFromString(path);
    }
}
