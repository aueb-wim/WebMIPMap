/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.UserMappingCorrespondences;
import it.unibas.spicy.persistence.DAOException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Exceptions;

/**
 *
 * @author ioannisxar
 */
public class ActionGetRecommendedScenario {
    
    private HashMap<String, String> commonScenarios;
    private OpenMappingScenario scenario;
    private String user, mappingName, mappingType;
    
    public ActionGetRecommendedScenario(HashMap<String, String> commonScenarios, String user, String mappingName, String mappingType){
        this.commonScenarios = commonScenarios;
        this.user = user;
        this.mappingName = mappingName;
        this.mappingType = mappingType;
    }
    
    public void performAction() throws DAOException, IOException{
        ArrayList<UserMappingCorrespondences> umc = new ArrayList<>();
        commonScenarios.forEach((user,mappingName)->{
            OpenMappingScenario scenarioToMatch = new OpenMappingScenario(user, mappingName);
            this.scenario = scenarioToMatch;
            try {
                umc.add(new UserMappingCorrespondences(user, mappingName, scenarioToMatch.getScenarioCorrespondences()));
            } catch (DAOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        GetRecommendedCorrespondences score = new GetRecommendedCorrespondences(umc, this.scenario);
        Map<String, Correspondence> recommendedCorrespondencesPerTarget = score.performAction();
        generateMappingTask(recommendedCorrespondencesPerTarget, this.scenario);
    }
    
    private void generateMappingTask(Map<String, Correspondence> correspondences, OpenMappingScenario scenario){
        copyFiles();
        try {
            scenario.getScenarioSchema("source", "public").printSchema();
            scenario.getScenarioSchema("target", "public").printSchema();
        } catch (DAOException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void copyFiles(){
        String initialPath = "";
        String destFolderPath = "";
        if(this.mappingType.equals("private")){
             initialPath = MipmapDirectories.getUserPrivatePath(user) + this.mappingName + "/";
             destFolderPath = MipmapDirectories.getUserPrivatePath(user) + "recommendedScenario" + "/";
        } else if(this.mappingType.equals("public")){
            initialPath = MipmapDirectories.getUserPublicPath(user) + this.mappingName + "/";
            destFolderPath = MipmapDirectories.getUserPublicPath(user) + "recommendedScenario" + "/";
        }
        File initDirSrc = new File(initialPath + "source");
        File initDirTarget = new File(initialPath + "target");
        System.out.println(initialPath);
        System.out.println(destFolderPath);
    }
}
