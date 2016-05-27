/*
    Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
    Giansalvatore Mecca - giansalvatore.mecca@unibas.it
    Salvatore Raunich - salrau@gmail.com
    Marcello Buoncristiano - marcello.buoncristiano@yahoo.it

    This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool
    
    ++Spicy is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    ++Spicy is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package gr.aueb.mipmapgui.controller.mapping;

import it.unibas.spicy.model.exceptions.IllegalMappingTaskException;
import it.unibas.spicy.model.mapping.ConstantFORule;
import it.unibas.spicy.model.mapping.FORule;
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
////import it.unibas.spicygui.vista.TGDCorrespondencesTopComponent;
////import it.unibas.spicygui.vista.TGDListTopComponent;

public class ActionViewTGDs {

    private Modello modello;
    private JSONObject TGDs = new JSONObject();
    private String TGDsString = "";

    public ActionViewTGDs(Modello modello) {
        this.modello = modello;
    }

   public void performAction(){
        //imported from TGDListTopComponent's createTGD()
        Scenario scenario = (Scenario) this.modello.getBean(Costanti.CURRENT_SCENARIO);
        MappingTask mappingTask = scenario.getMappingTask();
        JSONArray tgdArray = new JSONArray();
        StringBuilder result = new StringBuilder();
        for (FORule foRule : mappingTask.getMappingData().getSTTgds()) {
            JSONObject tgd = new JSONObject();
            tgd.put("tgd", foRule.toLogicalString(mappingTask));
            result.append(foRule.toLogicalString(mappingTask)).append("\n");
            tgdArray.add(tgd);
        }
        TGDs.put("tgds", tgdArray);
        JSONArray constTgdArray = new JSONArray();
        for (ConstantFORule constantFORule : mappingTask.getMappingData().getConstantSTTgds()) {
            JSONObject tgd = new JSONObject();
            tgd.put("constantTgd", constantFORule.toLogicalString(mappingTask));
            result.append(constantFORule.toLogicalString(mappingTask)).append("\n");
            constTgdArray.add(tgd);
         }
        TGDs.put("constantTgds", constTgdArray); 
        TGDsString = result.toString();
    }

    public JSONObject getTGDs() {
        return this.TGDs;
    }
    
    public String getTGDsString(){
        return this.TGDsString;
    }
}
