/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.UserMappingCorrespondences;
import it.unibas.spicy.persistence.DAOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
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
            String userClean = user.replace("%%%"+mappingName+"%%%", "");
            OpenMappingScenario scenarioToMatch = new OpenMappingScenario(userClean, mappingName);
            this.scenario = scenarioToMatch;
            try {
                umc.add(new UserMappingCorrespondences(userClean, mappingName, scenarioToMatch.getScenarioCorrespondences()));
            } catch (DAOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        GetRecommendedCorrespondences score = new GetRecommendedCorrespondences(umc, this.scenario);
        Map<String, Correspondence> recommendedCorrespondencesPerTarget = score.performAction();
        generateMappingTask(recommendedCorrespondencesPerTarget);
    }
    
    private void generateMappingTask(Map<String, Correspondence> correspondences) throws IOException{
        String initialPath = "";
        String destFolderPath = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String identifier = String.valueOf(timestamp.getTime());
        if(this.mappingType.equals("private")){
             initialPath = MipmapDirectories.getUserPrivatePath(user) + this.mappingName + "/";
             destFolderPath = MipmapDirectories.getUserPrivatePath(user) + "recommendedScenario_" + identifier + "/";
        } else if(this.mappingType.equals("public")){
            initialPath = MipmapDirectories.getUserPublicPath(user) + this.mappingName + "/";
            destFolderPath = MipmapDirectories.getUserPublicPath(user) + "recommendedScenario_" + identifier + "/";
        }
        File initDirSrc = new File(initialPath + "source");
        File initDirTarget = new File(initialPath + "target");
        File destDirSrc = new File(destFolderPath + "source");
        File destDirTarget = new File(destFolderPath + "target");
        //copy folders with source and target files
        FileUtils.copyDirectory(initDirSrc, destDirSrc);
        FileUtils.copyDirectory(initDirTarget, destDirTarget);
        //export recommended mapping task
        try (PrintWriter writer = new PrintWriter(destFolderPath + "mapping_task.xml", "UTF-8")) {
            //write schema
            writer.write(mappingTaskSchema(initialPath));
            writer.write("\t<correspondences>"+"\n");
            //TODO - write correspondences in xml
            correspondences.forEach((target, correspondence)->{
                if(!correspondence.getType().equals(Costanti.EMPTY_CORRESPONDENCE))
                    writer.write("\t<correspondence>"+"\n");
                
                switch (correspondence.getType()) {
                    case Costanti.SIMPLE_CORRESPONDENCE:
                        writer.write("\t\t<source-paths>"+"\n");
                        writer.write("\t\t<source-path>"+ correspondence.getSource()+"</source-path>"+"\n");
                        writer.write("\t\t</source-paths>"+"\n");
                        break;
                    case Costanti.CONSTANT_DATE:
                        writer.write("\t\t<source-paths />");
                        writer.write("\t\t<source-value>date()</source-value>"+"\n");
                        break;
                    case Costanti.CONSTANT_DATETIME:
                        writer.write("\t\t<source-paths />"+"\n");
                        writer.write("\t\t<source-value>datetime()</source-value>"+"\n");
                        break;
                    case Costanti.CONSTANT_DB_SEQUENCE:
                        writer.write("\t\t<source-paths />"+"\n");
                        //TODO
                        writer.write("\t\t<source-value>datetime()</source-value>"+"\n");
                        break;
                    case Costanti.CONSTANT_STRING:
                        writer.write("\t\t<source-paths />"+"\n");
                        writer.write("\t\t<source-value>"+correspondence.getSource()+"</source-value>"+"\n");
                        break;
                    case Costanti.CONSTANT_NUMBER:
                        writer.write("\t\t<source-paths />"+"\n");
                        writer.write("\t\t<source-value>"+correspondence.getSource()+"</source-value>"+"\n");
                        break;
                    case Costanti.FUNCTION:
                        writer.write("\t\t<source-paths>"+"\n");
                        writer.write("\t\t<source-path>"+ correspondence.getSource()+"</source-path>"+"\n");
                        writer.write("\t\t</source-paths>"+"\n");
                        break;
                    case Costanti.CONSTANT_SEQUENCE:
                        writer.write("\t\t<source-paths />"+"\n");
                        //TODO
                        writer.write("\t\t<source-value>datetime()</source-value>"+"\n");
                        break;
                    default:
                }
                if(!correspondence.getType().equals(Costanti.EMPTY_CORRESPONDENCE)){
                    writer.write("\t\t<target-path>"+ correspondence.getTarget()+"</target-path>"+"\n");
                    writer.write("\t\t<transformation-function>"+ correspondence.getTransformation()+"</transformation-function>"+"\n");
                    writer.write("\t\t<confidence>"+ correspondence.getFinalScore()+"</confidence>"+"\n");
                    writer.write("\t\t</correspondence>"+"\n");
                }
            });
            writer.write("\t</correspondences>"+"\n");
            writer.write("</mappingtask>");
        }        
    }
    
    private String mappingTaskSchema(String initialPath){
        File file = new File(initialPath+"mapping_task.xml");
        BufferedReader reader = null;
        String mappingText = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            
            while ((text = reader.readLine()) != null) {
                if(text.trim().equals("<correspondences>")){
                    break;
                }
                mappingText += text + "\n";
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return mappingText;
    }
}
