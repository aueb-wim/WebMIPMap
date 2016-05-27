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
import it.unibas.spicy.model.mapping.MappingTask;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenario;
import org.json.simple.JSONObject;
////import it.unibas.spicygui.vista.TransformationTopComponent;

public class ActionGenerateTransformations {

    //private ActionViewTransformation viewTransformationAction;
    private Modello modello;
    private JSONObject TGDs = new JSONObject();

    public ActionGenerateTransformations(Modello modello) {        
        this.modello=modello;
    }
    
    
    //web
    //Will need it later
    //also un-comment: private ActionViewTransformation viewTransformationAction;
    /*private void openWindows() {
        Scenario scenario = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
        TransformationTopComponent transformationTopComponent = scenario.getTransformationTopComponent();
        if (transformationTopComponent == null) {
            transformationTopComponent = new TransformationTopComponent(scenario);
            scenario.setTransformationTopComponent(transformationTopComponent);
        }
        transformationTopComponent.clear();
        this.viewTransformationAction.performAction();
    }*/
    
    public void performAction() {
        ActionViewTGDs actionViewTgds = new ActionViewTGDs(modello);
        actionViewTgds.performAction();        
        TGDs = actionViewTgds.getTGDs();
    }

    public JSONObject getTGDs() {
        return this.TGDs;
    }
   
    public String getName() {
        return Costanti.ACTION_SOLVING_MAPPING;
    }


}
