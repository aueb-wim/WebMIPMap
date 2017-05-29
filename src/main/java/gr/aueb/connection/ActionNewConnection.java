package gr.aueb.connection;

//giannisk, ioannisxar

import it.unibas.spicy.model.correspondence.ConstantValue;
import it.unibas.spicy.model.correspondence.DateFunction;
import it.unibas.spicy.model.correspondence.ISourceValue;
import it.unibas.spicy.model.correspondence.NewIdFunction;
import it.unibas.spicy.model.correspondence.DatetimeFunction;
import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.expressions.Expression;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicy.persistence.xml.DAOXmlUtility;
import it.unibas.spicy.utility.SpicyEngineConstants;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import it.unibas.spicy.model.correspondence.GetIdFromDb;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ActionNewConnection {
    private Modello modello;
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    private String scenarioNo;

    public ActionNewConnection(Modello model,String scenarioNo) {
        this.modello = model;
        this.scenarioNo=scenarioNo;
    }
    
    public void performAction(String[] sourcePathArray, String sourceValueText, String targetPath, String transformationText, 
            String type, String sequence, String offset, String dbProperties) {
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        
        List<PathExpression> sourcePaths = null;
        String fromPath="";
        if(sourcePathArray!=null){
            sourcePaths = new ArrayList<>();
            for(String sourcePathString : sourcePathArray){
                PathExpression pathExpression = generatePathExpression(sourcePathString);
                sourcePaths.add(pathExpression);
            }
            if (sourcePathArray.length==1)
                fromPath = sourcePathArray[0];
        }
        ISourceValue sourceValue = null;
        if (sourceValueText!=null &&!sourceValueText.equals("")){
            if (sourceValueText.equalsIgnoreCase(SpicyEngineConstants.SOURCEVALUE_DATE_FUNCTION)) {
                sourceValue = new DateFunction();
                sourceValue.setType(type);
            } else if (sourceValueText.equalsIgnoreCase(SpicyEngineConstants.SOURCEVALUE_DATETIME_FUNCTION)) {
                sourceValue = new DatetimeFunction();
                sourceValue.setType(type);
            } 
            else if (sourceValueText.equalsIgnoreCase(SpicyEngineConstants.SOURCEVALUE_NEWID_FUNCTION)) {
                sourceValue = new NewIdFunction();
                sourceValue.setType(type);
                sourceValue.setSequence(sequence);
                SpicyEngineConstants.OFFSET_MAPPING.put(sequence, offset);
                if(type.equals("getId()")){
                    JSONParser parser = new JSONParser();
                    try {
                        JSONObject jsonObject = (JSONObject) parser.parse(dbProperties);
                        String driver = (String) jsonObject.get("driver");
                        String uri = (String) jsonObject.get("uri");
                        String schema = (String) jsonObject.get("schema");
                        String username = (String) jsonObject.get("username");
                        String password = (String) jsonObject.get("password");
                        String table = (String) jsonObject.get("table");
                        String column = (String) jsonObject.get("column");
                        String function_value = (String) jsonObject.get("function_value");
                        SpicyEngineConstants.GET_ID_FROM_DB.put(sequence, 
                                new GetIdFromDb(driver, uri, schema, username, password, table, column, function_value));
                    }catch(ParseException e){
                        System.err.println(e.getMessage());
                    }
                }
            }
            else if(sourcePathArray == null && sourceValueText!=null &&!sourceValueText.equals("")){
                sourceValue = new ConstantValue(sourceValueText);
                sourceValue.setType(type);
            }
            fromPath = sourceValueText;
        }
        PathExpression targetPathExpression = generatePathExpression(targetPath);
        Expression transformationFunctionExpression = null;
        if(transformationText != null&&!transformationText.equals("")){
            transformationFunctionExpression = new Expression(DAOXmlUtility.cleanXmlString(transformationText));
            if (!fromPath.equals(transformationText))
               fromPath = transformationText;
        }
        double confidence = 1.0;
        ValueCorrespondence vc = new ValueCorrespondence(sourcePaths,sourceValue,targetPathExpression,transformationFunctionExpression,confidence);
        mappingTask.addCorrespondence(vc);        
        String key = fromPath + "->" + targetPath;
        scenario.addCorrespondence(key, vc);           
        //DUMMY CONFIG
        /*mappingTask.getConfig().setRewriteSubsumptions(true);
        mappingTask.getConfig().setRewriteCoverages(true);
        mappingTask.getConfig().setRewriteSelfJoins(true);
        mappingTask.getConfig().setRewriteEGDs(false);
        mappingTask.getConfig().setSortStrategy(-1);
        mappingTask.getConfig().setSkolemTableStrategy(-1);
        mappingTask.getConfig().setUseLocalSkolem(false);*/       
    }
    
    private PathExpression generatePathExpression(String sourcePath) {
        return pathGenerator.generatePathFromString(sourcePath);
    }

}
