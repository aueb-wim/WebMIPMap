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

package gr.aueb.mipmapgui.view.tree;

import gr.aueb.mipmapgui.Costanti;
import gr.aueb.mipmapgui.controller.Scenario;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.model.paths.PathExpression;
import it.unibas.spicy.model.paths.operators.GeneratePathExpression;
import it.unibas.spicygui.commons.Modello;
import java.util.HashMap;


public class ActionDeleteDuplicateNode {
     private Modello modello;
    private String scenarioNo;
    private IDataSourceProxy dataSource;
    private GeneratePathExpression pathGenerator = new GeneratePathExpression();
    
    public ActionDeleteDuplicateNode(Modello modello, String scenarioNo, boolean isSource) {        
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
        dataSource.removeDuplication(pathExpression);
    }
   
    private PathExpression generatePathExpression(String path) {
        return pathGenerator.generatePathFromString(path);
    }
}
