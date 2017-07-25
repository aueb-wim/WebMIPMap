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

package gr.aueb.mipmapgui.controller.file;

import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.HashMap;

//giannisk
public class ActionRemoveMappingTask {
        private Modello modello;    
    
    public ActionRemoveMappingTask(Modello modello) {
        this.modello=modello;
    }
    
    public void performAction(String scenarioNo){
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        //set current scenario as null if the current scenario is the one being removed
        if (scenarioMap.get(Integer.valueOf(scenarioNo)).equals(modello.getBean(Costanti.CURRENT_SCENARIO))){
            modello.putBean(Costanti.CURRENT_SCENARIO, null);
            //modello.putBean(Costanti.CURRENT_SCENARIO, null);
        }
        //delete the scenario that is removed from the mapper
        scenarioMap.put(Integer.valueOf(scenarioNo),null);
    }
}
