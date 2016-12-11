/* Copyright 2015-2016 by the Athens University of Economics and Business (AUEB).
   This file is part of WebMIPMap.
   WebMIPMap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    WebMIPMap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with WebMIPMap.  If not, see <http://www.gnu.org/licenses/>.
 */

package gr.aueb.connection;

//giannisk

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class ActionNewConnection {
    private Modello modello;
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    private String scenarioNo;

    public ActionNewConnection(Modello model,String scenarioNo) {
        this.modello = model;
        this.scenarioNo=scenarioNo;
    }
    
    public void performAction(String[] sourcePathArray, String sourceValueText, String targetPath, String transformationText) {
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        Scenario scenario = scenarioMap.get(Integer.valueOf(scenarioNo));
        MappingTask mappingTask = scenario.getMappingTask();
        
        List<PathExpression> sourcePaths = null;
        String fromPath="";
        if(sourcePathArray!=null){
            sourcePaths = new ArrayList<PathExpression>();
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
            } else if (sourceValueText.equalsIgnoreCase(SpicyEngineConstants.SOURCEVALUE_DATETIME_FUNCTION)) {
                sourceValue = new DatetimeFunction();
            } 
            else if (sourceValueText.equalsIgnoreCase(SpicyEngineConstants.SOURCEVALUE_NEWID_FUNCTION)) {
                sourceValue = new NewIdFunction();
            } 
            else {
                sourceValue = new ConstantValue(sourceValueText);
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
