package gr.aueb.controllers;

import gr.aueb.connection.ActionDeleteConnection;
import gr.aueb.connection.ActionDeleteJoinCondition;
import gr.aueb.connection.ActionJoinConditionChangeOptions;
import gr.aueb.connection.ActionNewConnection;
import gr.aueb.connection.ActionNewJoinCondition;
import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.Scenario;
import gr.aueb.mipmapgui.controller.file.ActionLoadTrustedUserMappings;
import gr.aueb.mipmapgui.controller.file.ActionDeleteMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionGetDbOffset;
import gr.aueb.mipmapgui.controller.file.ActionInitialize;
import gr.aueb.mipmapgui.controller.file.ActionNewMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionOpenMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionRemoveMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionSaveMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionSelectMappingTask;
import gr.aueb.mipmapgui.controller.file.ActionZipDirectory;
import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import gr.aueb.mipmapgui.controller.mapping.ActionGenerateTransformations;
import gr.aueb.mipmapgui.controller.mapping.ActionViewSql;
import gr.aueb.mipmapgui.controller.mapping.ActionViewTGDs;
import gr.aueb.mipmapgui.controller.mapping.ActionViewTransformations;
import gr.aueb.mipmapgui.controller.mapping.ActionViewXQuery;
import gr.aueb.mipmapgui.view.tree.ActionDeleteDuplicateNode;
import gr.aueb.mipmapgui.view.tree.ActionDuplicateNode;
import gr.aueb.mipmapgui.view.tree.ActionSelectionCondition;
import gr.aueb.users.ActionAnswerTrustRequest;
import gr.aueb.users.ActionGetUsers;
import gr.aueb.users.ActionSendTrustRequest;
import gr.aueb.users.ActionUpdatePercentage;
import gr.aueb.users.recommendation.ActionFindCommonMappingTasks;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicygui.commons.Modello;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
// ioannisxar
@RestController
public class MappingController {
        
    private Modello modello;   
    public static String user;
      
    @RequestMapping(value="/Initialize", method=RequestMethod.POST, produces="text/plain")
    //@ResponseBody
    public String initializeInfo(Principal principal) {
        MappingController.user = principal.getName();
        
        initialize();
        ActionInitialize actionInitialize = new ActionInitialize();
        actionInitialize.performAction(user);
        JSONObject outputObject = actionInitialize.getJSONObject();
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value="/ListUsers", method=RequestMethod.POST, produces="text/plain")
    public String getUsersInfo(Principal principal) {
        ActionGetUsers actionGetUsers = new ActionGetUsers();
        actionGetUsers.performAction(user);
        JSONObject outputObject = actionGetUsers.getJSONObject();
        return outputObject.toJSONString();
    }
    
    //working - xarchakos
    @RequestMapping(value="/LoadTrustedUserMappings", method=RequestMethod.POST, produces="text/plain")
    public String loadTrustedUserMappings(@RequestParam("openName") String openName) {
        ActionLoadTrustedUserMappings actionLoadTrustedUserMappings = new ActionLoadTrustedUserMappings();
        actionLoadTrustedUserMappings.performAction(openName);
        return actionLoadTrustedUserMappings.getMappings().toJSONString();
    }

    @RequestMapping(value="/sendTrustRequest", method=RequestMethod.POST, produces="text/plain")
    public String sendTrustRequest(Principal principal, @RequestParam("userId") String id) {
        ActionSendTrustRequest trustRequest = new ActionSendTrustRequest();
        trustRequest.performAction(principal.getName(), id);
        return trustRequest.getMessage().toJSONString();
    }
    
    //working - xarchakos
    @RequestMapping(value="/answerTrustRequest", method=RequestMethod.POST, produces="text/plain")
    public String answerTrustRequest(Principal principal, @RequestParam("userId") String id, @RequestParam("statusCode") String statusCode) {
        ActionAnswerTrustRequest answerTrustRequest = new ActionAnswerTrustRequest();
        answerTrustRequest.performAction(principal.getName(), id, statusCode);
        return answerTrustRequest.getMessage().toJSONString();
    }
    
    
    @RequestMapping(value="/OpenMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String openMappingTaskInfo(@RequestParam("openName") String openName, @RequestParam("scenarioNo") String scenarioNo, 
            @RequestParam("global") boolean global, @RequestParam("userPublic") boolean userPublic, @RequestParam("trustedUser") String trustedUser) {
        ActionOpenMappingTask actionOpenMapTask = new ActionOpenMappingTask(modello, Integer.valueOf(scenarioNo));
        actionOpenMapTask.performAction(openName, user, global, userPublic, trustedUser);
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
    public String saveMappingTaskInfo(@RequestParam("saveName") String saveName, @RequestParam("scenarioNo") String scenarioNo, 
            @RequestParam("overwrite") boolean overwrite, @RequestParam(value="previousName", required=false) String previousName, 
            @RequestParam(value="fromGlobal", required=false) boolean fromGlobal, @RequestParam(value="fromTrustedUser", required=false) String fromTrustedUser, 
            @RequestParam(value="trustedUser", required=false) String trustedUser, @RequestParam(value="acceptedConns", required=false) String acceptedConns,
            @RequestParam(value="totalConns", required=false) String totalConns) throws Exception {
        
        if ((previousName == null || previousName.equals("")) && overwrite)
            throw new Exception("Cannot save mapping task.");
        
        ActionSaveMappingTask actionSaveMapTask = new ActionSaveMappingTask(modello, Integer.valueOf(scenarioNo)); 
        boolean fromUser = Boolean.parseBoolean(fromTrustedUser);
        actionSaveMapTask.performAction(saveName, user, overwrite, previousName, fromGlobal, fromUser, trustedUser, false, false);
        if (fromUser && StringUtils.isNumeric(acceptedConns) && StringUtils.isNumeric(totalConns) && !user.equals(trustedUser)) {
            Integer acceptedConnections = Integer.valueOf(acceptedConns);
            Integer totalConnections = Integer.valueOf(totalConns);
            if (totalConnections > 0) {
                ActionUpdatePercentage updatePercentage = new ActionUpdatePercentage();
                updatePercentage.performAction(trustedUser, acceptedConnections, totalConnections);
            }
        }
        JSONObject outputObject = new JSONObject();
        outputObject.put("saveName",saveName); 
        return outputObject.toJSONString();
    }
    
    @RequestMapping(value="/SaveMappingTaskGlobal", method=RequestMethod.POST, produces="text/plain")
    public String saveMappingTaskGlobalInfo(HttpServletRequest request, @RequestParam("saveName") String saveName, @RequestParam("scenarioNo") String scenarioNo, 
            @RequestParam("overwrite") boolean overwrite, @RequestParam(value="previousName", required=false) String previousName, 
            @RequestParam(value="fromGlobal", required=false) boolean fromGlobal, @RequestParam(value="fromTrustedUser", required=false) boolean fromTrustedUser, 
            @RequestParam(value="trustedUser", required=false) String trustedUser) throws Exception {
                
        //if (!request.isUserInRole("ROLE_ADMIN")) {  
            if ((previousName == null || previousName.equals("")) && overwrite)
                throw new Exception("Cannot save mapping task.");

            ActionSaveMappingTask actionSaveMapTask = new ActionSaveMappingTask(modello, Integer.valueOf(scenarioNo));
            actionSaveMapTask.performAction(saveName, user, overwrite, previousName, fromGlobal, fromTrustedUser, trustedUser, true, false);
        //}
        //else exception
        JSONObject outputObject = new JSONObject();
        outputObject.put("saveName",saveName);
        
        return outputObject.toJSONString();
    }
    
    //working - xarchakos
    //Possibly calculation and update of accepted connection
    @RequestMapping(value="/SaveMappingTaskPublic", method=RequestMethod.POST, produces="text/plain")
    public String SaveMappingTaskPublicInfo(HttpServletRequest request, @RequestParam("saveName") String saveName, @RequestParam("scenarioNo") String scenarioNo, 
            @RequestParam("overwrite") boolean overwrite, @RequestParam(value="previousName", required=false) String previousName, 
            @RequestParam(value="fromGlobal", required=false) boolean fromGlobal, @RequestParam(value="fromTrustedUser", required=false) boolean fromTrustedUser, 
            @RequestParam(value="trustedUser", required=false) String trustedUser) throws Exception {
            
        if ((previousName == null || previousName.equals("")) && overwrite)
            throw new Exception("Cannot save mapping task.");

        ActionSaveMappingTask actionSaveMapTask = new ActionSaveMappingTask(modello, Integer.valueOf(scenarioNo));
        actionSaveMapTask.performAction(saveName, user, overwrite, previousName, fromGlobal, fromTrustedUser, trustedUser, false, true);
        JSONObject outputObject = new JSONObject();
        outputObject.put("saveName",saveName);
        
        return outputObject.toJSONString();
    }
    
        
    @RequestMapping(value="/DeleteMappingTask", method=RequestMethod.POST, produces="text/plain")
    public String deleteMappingTaskInfo(@RequestParam("deleteName") String deleteName) {
        ActionDeleteMappingTask actionDeleteMapTask = new ActionDeleteMappingTask(modello);
        actionDeleteMapTask.performAction(deleteName, user, false);
        JSONObject outputObject = new JSONObject();
        outputObject.put("deleteName",deleteName);       
        return outputObject.toJSONString();
    }  
    
    //working - xarchakos
    @RequestMapping(value="/DeleteMappingTaskPublic", method=RequestMethod.POST, produces="text/plain")
    public String deleteMappingTaskInfoPublic(@RequestParam("deleteName") String deleteName) {
        ActionDeleteMappingTask actionDeleteMapTask = new ActionDeleteMappingTask(modello);
        actionDeleteMapTask.performAction(deleteName, user, true);
        JSONObject outputObject = new JSONObject();
        outputObject.put("deleteName",deleteName);       
        return outputObject.toJSONString();
    } 
    
    @RequestMapping(value="/EstablishedConnection", method=RequestMethod.POST, produces="text/plain")
    public String connectionInfo(@RequestParam(value="sourcePathArray[]",required=false) String[] sourcePathArray, @RequestParam("sourceValue") String sourceValueText,
            @RequestParam("targetPath") String targetPath,@RequestParam("expression") String transformationText, 
            @RequestParam("scenarioNo") String scenarioNo, @RequestParam("type") String type, 
            @RequestParam("sequence") String sequence, @RequestParam("offset") String offset, @RequestParam(value="dbProperties", required=false) String dbProperties) {
        ActionNewConnection newConnection = new ActionNewConnection(modello, scenarioNo);                    
        newConnection.performAction(sourcePathArray, sourceValueText, targetPath ,transformationText, type, sequence, offset, dbProperties); 
        JSONObject outputObject = new JSONObject();
        return outputObject.toJSONString();
    } 
        
    @RequestMapping(value="/UpdateConnection", method=RequestMethod.POST, produces="text/plain")
    public String updateConnectionInfo(@RequestParam("sourcePathArray[]") String[] sourcePathArray, @RequestParam("sourceValue") String sourceValueText,
            @RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath,
            @RequestParam("expression") String transformationText, @RequestParam("scenarioNo") String scenarioNo,
            @RequestParam("type") String type, @RequestParam("sequence") String sequence, @RequestParam("offset") String offset, 
            @RequestParam(value="dbProperties", required=false) String dbProperties) {       
        ActionDeleteConnection deleteConnection = new ActionDeleteConnection(modello, scenarioNo);
        deleteConnection.performAction(sourcePath, targetPath);
        ActionNewConnection newConnection = new ActionNewConnection(modello,scenarioNo); 
        newConnection.performAction(sourcePathArray, sourceValueText, targetPath ,transformationText, type, sequence, offset, dbProperties); 
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
        deleteConnection.performActionManyConnections(sourcePathArray, targetPathArray); 
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
    
    @RequestMapping(value="/DownloadMappingTask", method=RequestMethod.GET)
    public byte[] downloadTask(HttpServletResponse response, @RequestParam("downloadName") String downloadName) throws IOException, Exception { 
        String path = MipmapDirectories.getUserPrivatePath(user) + downloadName;
        ActionZipDirectory actionZipDir = new ActionZipDirectory(modello);
        byte[] zip = actionZipDir.performAction(path);
        response.reset();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename="+downloadName+".zip");         
        return zip;
    }
    
    @RequestMapping(value="/DownloadMappingTaskPublic", method=RequestMethod.GET)
    public byte[] downloadTaskPublic(HttpServletResponse response, @RequestParam("downloadName") String downloadName) throws IOException, Exception { 
        String path = MipmapDirectories.getUserPublicPath(user) + downloadName;
        ActionZipDirectory actionZipDir = new ActionZipDirectory(modello);
        byte[] zip = actionZipDir.performAction(path);
        response.reset();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename="+downloadName+".zip");         
        return zip;
    }
    
    
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex){ 
        JSONObject outputObject = new JSONObject();
        //outputObject.put("exception","Server exception: "+ex.getClass().getName()+": "+ex.getMessage());
        outputObject.put("exception","Server exception: "+ex.getMessage());
        return outputObject.toJSONString();
    }
          
    @RequestMapping(value="/GetDBOffset", method=RequestMethod.POST, produces="text/plain")
    public String getDBOffset(@RequestParam("dbProperties") String dbProperties) throws SQLException, DAOException {       
        ActionGetDbOffset gdo = new ActionGetDbOffset(dbProperties);
        String offset = gdo.performAction();
        return offset;
    } 
    
    @RequestMapping(value="/RecommendMappingTask", method=RequestMethod.GET, produces="text/plain")
    public String RecommendMappingTask(@RequestParam("openedMappingName") String openedMappingName) throws DAOException, IOException {  
        ActionFindCommonMappingTasks commonMappings = new ActionFindCommonMappingTasks(user, openedMappingName);
        HashMap<String, String> commonScenarios = commonMappings.findCommonScenarions();
        if(commonScenarios.isEmpty()){
            return "No common scenarios have found";
        }
        return openedMappingName;
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
