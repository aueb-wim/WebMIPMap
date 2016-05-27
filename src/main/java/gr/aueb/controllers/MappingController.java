package gr.aueb.controllers;

import gr.aueb.connection.ActionDeleteConnection;
import gr.aueb.connection.ActionDeleteJoinCondition;
import gr.aueb.connection.ActionJoinConditionChangeOptions;
import gr.aueb.connection.ActionNewConnection;
import gr.aueb.connection.ActionNewJoinCondition;
import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.Scenario;
import gr.aueb.mipmapgui.controller.file.ActionDeleteMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionInitialize;
import gr.aueb.mipmapgui.controller.file.ActionNewMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionOpenMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionRemoveMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionSaveMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionSelectMappingTask;
import gr.aueb.mipmapgui.controller.mapping.ActionGenerateTransformations;
import gr.aueb.mipmapgui.controller.mapping.ActionViewSql;
import gr.aueb.mipmapgui.controller.mapping.ActionViewTGDs;
import gr.aueb.mipmapgui.controller.mapping.ActionViewTransformations;
import gr.aueb.mipmapgui.controller.mapping.ActionViewXQuery;
import gr.aueb.mipmapgui.view.tree.ActionDeleteDuplicateNode;
import gr.aueb.mipmapgui.view.tree.ActionDuplicateNode;
import gr.aueb.mipmapgui.view.tree.ActionSelectionCondition;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicygui.commons.Modello;
import java.security.Principal;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {
        
    private Modello modello;   
    public static String user;
      
    @RequestMapping(value="/Initialize", method=RequestMethod.POST, produces="text/plain")
    //@ResponseBody
    public String initializeInfo(Principal principal) {
        this.user = principal.getName();
        initialize();
        ActionInitialize actionInitialize = new ActionInitialize(modello);
        actionInitialize.performAction(user);
        JSONObject outputObject = actionInitialize.getJSONObject();
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value="/OpenMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String openMappingTaskInfo(@RequestParam("openName") String openName, @RequestParam("scenarioNo") String scenarioNo) {
        ActionOpenMappingTask actionOpenMapTask = new ActionOpenMappingTask(modello, Integer.valueOf(scenarioNo));
        actionOpenMapTask.performAction(openName, user);
        JSONObject outputObject = actionOpenMapTask.getSchemaTreesObject();
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value="/NewMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String newMappingTaskInfo(HttpServletRequest request) throws DAOException {
        ActionNewMappingTask actionNewMapTask = new ActionNewMappingTask(modello, user);
        actionNewMapTask.performAction(request);
        JSONObject outputObject = actionNewMapTask.getSchemaTreesObject(); 
        return outputObject.toJSONString();
    }    
    
    @RequestMapping(value="/SaveMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String saveMappingTaskInfo(@RequestParam("saveName") String saveName, @RequestParam("scenarioNo") String scenarioNo) {
        ActionSaveMappingTask actionSaveMapTask = new ActionSaveMappingTask(modello, Integer.valueOf(scenarioNo));
        actionSaveMapTask.performAction(saveName, user);
        JSONObject outputObject = new JSONObject();
        outputObject.put("saveName",saveName); 
        return outputObject.toJSONString();
    }  
        
    @RequestMapping(value="/DeleteMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String deleteMappingTaskInfo(@RequestParam("deleteName") String deleteName) {
        ActionDeleteMappingTask actionDeleteMapTask = new ActionDeleteMappingTask(modello);
        actionDeleteMapTask.performAction(deleteName, user);
        JSONObject outputObject = new JSONObject();
        outputObject.put("deleteName",deleteName);       
        return outputObject.toJSONString();
    }  
    
    @RequestMapping(value="/EstablishedConnection", method=RequestMethod.POST, produces="text/plain")
    public String connectionInfo(@RequestParam(value="sourcePathArray[]",required=false) String[] sourcePathArray, @RequestParam("sourceValue") String sourceValueText,
            @RequestParam("targetPath") String targetPath,@RequestParam("expression") String transformationText, @RequestParam("scenarioNo") String scenarioNo) {      
        ActionNewConnection newConnection = new ActionNewConnection(modello, scenarioNo);                    
        newConnection.performAction(sourcePathArray, sourceValueText, targetPath ,transformationText); 
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
        
    @RequestMapping(value="/UpdateConnection", method=RequestMethod.POST, produces="text/plain")
    public String updateConnectionInfo(@RequestParam("sourcePathArray[]") String[] sourcePathArray, @RequestParam("sourceValue") String sourceValueText,
            @RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath,
            @RequestParam("expression") String transformationText, @RequestParam("scenarioNo") String scenarioNo) {       
        ActionDeleteConnection deleteConnection = new ActionDeleteConnection(modello, scenarioNo);
        deleteConnection.performAction(sourcePath, targetPath);
        ActionNewConnection newConnection = new ActionNewConnection(modello,scenarioNo); 
        newConnection.performAction(sourcePathArray, sourceValueText, targetPath ,transformationText); 
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/DeleteConnection", method=RequestMethod.POST, produces="text/plain")
    public String deleteConnectionInfo(@RequestParam(value="sourcePath", required=false) String sourcePath, 
            @RequestParam(value="targetPath", required=false) String targetPath, @RequestParam("scenarioNo") String scenarioNo) {       
        ActionDeleteConnection deleteConnection = new ActionDeleteConnection(modello,scenarioNo);
        deleteConnection.performAction(sourcePath, targetPath);
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/DeleteAllConnections", method=RequestMethod.POST, produces="text/plain")
    public String deleteAllConnectionsInfo(@RequestParam(value="sourcePathArray[]", required=false) String[] sourcePathArray, 
            @RequestParam(value="targetPathArray[]", required=false) String[] targetPathArray, @RequestParam("scenarioNo") String scenarioNo) {       
        ActionDeleteConnection deleteConnection = new ActionDeleteConnection(modello,scenarioNo);
        deleteConnection.performActionAllConnections(sourcePathArray, targetPathArray); 
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/NewJoinCondition", method=RequestMethod.POST, produces="text/plain")
    public String newJoinConditionInfo(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath,
            @RequestParam("isSource") boolean isSource, @RequestParam("scenarioNo") String scenarioNo) {  
        ActionNewJoinCondition newJoin = new ActionNewJoinCondition(modello,scenarioNo);
        newJoin.performAction(sourcePath,targetPath,isSource); 
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/JoinConditionOptions", method=RequestMethod.POST, produces="text/plain")
    public String joinConditionOptionsInfo(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath,
            @RequestParam("isSource") boolean isSource, @RequestParam("changedOption") String changedOption, @RequestParam("scenarioNo") String scenarioNo) {  
        ActionJoinConditionChangeOptions changeJoin = new ActionJoinConditionChangeOptions(modello,scenarioNo);
        changeJoin.performAction(changedOption, sourcePath, targetPath, isSource); 
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/DeleteJoin", method=RequestMethod.POST, produces="text/plain")
    public String deleteJoinConditionInfo(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath,
            @RequestParam("isSource") boolean isSource, @RequestParam("scenarioNo") String scenarioNo) {  
        ActionDeleteJoinCondition deleteJoinCondition = new ActionDeleteJoinCondition(modello,scenarioNo);
        deleteJoinCondition.performAction(sourcePath, targetPath, isSource);  
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/SelectMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String selectMappingTaskInfo(@RequestParam("scenarioNo") String scenarioNo) {  
        ActionSelectMappingTask selectMappingTask = new ActionSelectMappingTask(modello);
        selectMappingTask.performAction(scenarioNo);                    
        JSONObject outputObject = new JSONObject();
        outputObject.put("scenarioNo",scenarioNo); 
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/DuplicateNode", method=RequestMethod.POST, produces="text/plain")
    public String duplicateNodeInfo(@RequestParam("sourcePath") String sourcePath, @RequestParam("isSource") boolean isSource, @RequestParam("scenarioNo") String scenarioNo) {  
        ActionDuplicateNode duplicateNode = new ActionDuplicateNode(modello, scenarioNo, isSource);
        duplicateNode.performAction(sourcePath);                     
        JSONObject outputObject = new JSONObject();
        outputObject.put("scenarioNo",scenarioNo); 
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/DeleteDuplicateNode", method=RequestMethod.POST, produces="text/plain")
    public String deleteDuplicateNodeInfo(@RequestParam("sourcePath") String sourcePath, @RequestParam("isSource") boolean isSource, @RequestParam("scenarioNo") String scenarioNo) {  
        ActionDeleteDuplicateNode deleteDuplicateNode = new ActionDeleteDuplicateNode(modello, scenarioNo, isSource);
        deleteDuplicateNode.performAction(sourcePath);                      
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/Generate", method=RequestMethod.POST, produces="text/plain")
    public String generateTGDsInfo() {       
        ActionGenerateTransformations generateTransformations = new ActionGenerateTransformations(modello);
        generateTransformations.performAction();                      
        JSONObject outputObject = generateTransformations.getTGDs();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/ShowMappingTaskInfo", method=RequestMethod.POST, produces="text/plain")
    public String deleteDuplicateNodeInfo(@RequestParam("scenarioNo") String scenarioNo) {  
        ActionViewTransformations viewTransformationInfo = new ActionViewTransformations(modello, scenarioNo);                                   
        JSONObject outputObject = new JSONObject();
        outputObject.put("info",viewTransformationInfo.performAction());
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/SqlOutput", method=RequestMethod.POST, produces="text/plain")
    public String sqlOutput(@RequestParam("scenarioNo") String scenarioNo) {  
        ActionViewSql viewSqlScript = new ActionViewSql(modello, Integer.valueOf(scenarioNo));                                   
        JSONObject outputObject = new JSONObject();
        outputObject.put("sqlScript",viewSqlScript.performAction()); 
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/XqueryOutput", method=RequestMethod.POST, produces="text/plain")
    public String xqueryOutput(@RequestParam("scenarioNo") String scenarioNo) {  
        ActionViewXQuery viewXQueryScript = new ActionViewXQuery(modello, scenarioNo);                                   
        JSONObject outputObject = new JSONObject();
        outputObject.put("xQueryScript",viewXQueryScript.performAction());  
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/EditSelectionCondition", method=RequestMethod.POST, produces="text/plain")
    public String editSelCondInfo(@RequestParam("specificAction") String specificAction, @RequestParam("path") String path,
            @RequestParam("expression") String expression, @RequestParam("scenarioNo") String scenarioNo) {  
        ActionSelectionCondition editSelectionCondition = new ActionSelectionCondition(modello, scenarioNo);
        editSelectionCondition.performAction(specificAction, path, expression);                                  
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/RemoveMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String removeTaskInfo(@RequestParam("scenarioNo") String scenarioNo) {  
        ActionRemoveMappingTask removeMappingTask = new ActionRemoveMappingTask(modello);
        removeMappingTask.performAction(scenarioNo);                                  
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/ExportTgds", method=RequestMethod.GET, produces="text/plain")
    public String exportTgds(HttpServletResponse response) {  
        String tgdsString = "";
        response.reset();
        response.setContentType("text/plain");                    
        response.setHeader("Content-Disposition","attachment; filename=tgd.txt");
        ActionViewTGDs actionViewTgds = new ActionViewTGDs(modello);
        actionViewTgds.performAction();        
        tgdsString = actionViewTgds.getTGDsString();            
        return tgdsString;
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex){ 
        JSONObject outputObject = new JSONObject();
        //outputObject.put("exception","Server exception: "+ex.getClass().getName()+": "+ex.getMessage());
        outputObject.put("exception","Server exception: "+ex.getMessage());
        return outputObject.toJSONString();
    }   
               
    private void initialize(){
       if (this.modello == null) {
           modello = new Modello();
       }
       if (this.modello.getBean(Costanti.SCENARIO_MAPPER)==null){
           this.modello.putBean(Costanti.SCENARIO_MAPPER, new HashMap<Integer, Scenario>());
       }
    }
}
