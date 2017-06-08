/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.MappingScenario;
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
import java.util.Random;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.io.FileUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author ioannisxar
 */
public class ActionGetRecommendedScenario {
    
    private HashMap<MappingScenario, String> commonScenarios;
    private OpenMappingScenario scenario;
    private String user, mappingName, mappingType;
    
    public ActionGetRecommendedScenario(HashMap<MappingScenario, String> commonScenarios, String user, String mappingName, String mappingType){
        this.commonScenarios = commonScenarios;
        this.user = user;
        this.mappingName = mappingName;
        this.mappingType = mappingType;
    }
    
    public String performAction() throws DAOException, IOException, FileNotFoundException, JSQLParserException{
        ArrayList<UserMappingCorrespondences> umc = new ArrayList<>();
        commonScenarios.forEach((sc,mapping)->{
            String userClean = sc.getUserName();
            OpenMappingScenario scenarioToMatch = new OpenMappingScenario(userClean, mapping);
            this.scenario = scenarioToMatch;
            try {
                umc.add(new UserMappingCorrespondences(userClean, mapping, scenarioToMatch.getScenarioCorrespondences()));
            } catch (DAOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        GetRecommendedCorrespondences score = new GetRecommendedCorrespondences(umc, this.scenario);
        Map<String, Correspondence> recommendedCorrespondencesPerTarget = score.performAction();
        String identifier = generateMappingTask(recommendedCorrespondencesPerTarget);
        return "recommendedScenario_" + identifier;
    }
    
    private String generateMappingTask(Map<String, Correspondence> correspondences) throws IOException{
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
            //write correspondences in xml
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
                        String seq = generateString(6);
                        writer.write("\t\t<source-paths />"+"\n");
                        writer.write("\t\t<source-value>" + "\n");
                        writer.write("\t\tnewId()_"+ seq + "\n");
                        writer.write("\t\t<sequence>"+ seq +"</sequence>"+"\n");
                        writer.write("\t\t<offset>0</offset>"+"\n");
                        writer.write("\t\t</source-value>" + "\n");
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
                        String seq1 = generateString(6);
                        writer.write("\t\t<source-paths />"+"\n");
                        writer.write("\t\t<source-value>" + "\n");
                        writer.write("\t\tnewId()_"+ seq1 + "\n");
                        writer.write("\t\t<sequence>"+ seq1 +"</sequence>"+"\n");
                        writer.write("\t\t<offset>"+ correspondence.getOffset()+"</offset>"+"\n");
                        writer.write("\t\t</source-value>" + "\n");
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
        return identifier;
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
    
    private String generateString(int length){
        Random rng = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890";
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
            text[i] = alphabet.charAt(rng.nextInt(alphabet.length()));
        return new String(text);
    }
}
