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
package gr.aueb.mipmapgui.controller;

import it.unibas.spicy.model.correspondence.ValueCorrespondence;
import it.unibas.spicy.model.datasource.JoinCondition;
import it.unibas.spicy.model.mapping.FORule;
import it.unibas.spicy.model.mapping.MappingTask;

import gr.aueb.mipmapgui.Costanti;
//import it.unibas.spicygui.Utility;
import it.unibas.spicygui.commons.AbstractScenario;
/*import it.unibas.spicygui.vista.BestMappingsTopComponent;
import it.unibas.spicygui.vista.CompositionTopComponent;
import it.unibas.spicygui.vista.InstancesTopComponent;
import it.unibas.spicygui.vista.MappingTaskTopComponent;
import it.unibas.spicygui.vista.RankedTransformationsTopComponent;
import it.unibas.spicygui.vista.SelectedTransformationTopComponent;
import it.unibas.spicygui.vista.SpicyTopComponent;
import it.unibas.spicygui.vista.SqlTopComponent;
import it.unibas.spicygui.vista.TGDCorrespondencesTopComponent;
import it.unibas.spicygui.vista.TGDListTopComponent;
import it.unibas.spicygui.vista.TransformationTopComponent;
import it.unibas.spicygui.vista.XQueryTopComponent;*/
import gr.aueb.mipmapgui.view.configuration.EngineConfigurationPM;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Scenario extends AbstractScenario  {

    public static int X_OFFSET_IMAGE_NUMBER = 14;
    public static int Y_OFFSET_IMAGE_NUMBER = -1;
    private static Log logger = LogFactory.getLog(Scenario.class);
    private String ID = null;
    private transient Image imageNumber = null;
    private int number;
    private Object stato;
    private boolean selected;
    private transient File saveFile;
    private transient FORule selectedFORule;
    private boolean tgdSession;
//    private boolean modified;
//    private boolean inComposition;
    private transient List<Scenario> relatedScenarios = new ArrayList<Scenario>();
    //TopComponent
    private transient MappingTask mappingTask;
    private transient EngineConfigurationPM engineConfigurationPM;

    //giannisk
    private transient HashMap<String,ValueCorrespondence> vcMap;
    private transient HashMap<String,JoinCondition> jcMap;

    public Scenario(String ID, MappingTask mappingTask, boolean selected) {
        this.ID = ID;
        this.mappingTask = mappingTask;
        this.engineConfigurationPM = new EngineConfigurationPM(mappingTask.getConfig());
//        this.mappingTaskTopComponent = new MappingTaskTopComponent(this);
//        this.mappingTaskTopComponent = new MappingTaskTopComponent(this, getImageNumber());
//        this.instancesTopComponent = new InstancesTopComponent(this);
        this.selected = selected;
        vcMap = new HashMap<>();
        jcMap = new HashMap<>();
    }

    public Scenario(String ID, MappingTask mappingTask, boolean selected, File saveFile) {
        this.saveFile = saveFile;
        this.ID = ID;
        this.mappingTask = mappingTask;
        this.engineConfigurationPM = new EngineConfigurationPM(mappingTask.getConfig());
        this.selected = selected;
        vcMap = new HashMap<>();
        jcMap = new HashMap<>();
    }
    
    public ValueCorrespondence getCorrespondence(String key){
        return vcMap.get(key);
    }
    
    public void addCorrespondence(String key, ValueCorrespondence vc){
        vcMap.put(key, vc);
    }
    
    public void removeCorrespondence(String key) {
        vcMap.remove(key);
    }
    
    public JoinCondition getJoinCondition(String key){
        return jcMap.get(key);
    }
    
    public void addJoinCondition(String key, JoinCondition jc){
        jcMap.put(key, jc);
    }
    
    public void removeJoinCondition(String key) {
        jcMap.remove(key);
    }

    public void addSourceInstance(String absolutePath) {
        setChanged();
        notifyObservers(Costanti.SOURCE);
    }

    public void addTargetInstance(String absolutePath) {
        setChanged();
        notifyObservers(Costanti.TARGET);
    }

    public void initializeScenario(int number) {
        this.setNumber(number);
    }
    // <editor-fold defaultstate="collapsed" desc="Getter & Setter -- TopComponent">

    /*public InstancesTopComponent getInstancesTopComponent() {
        return instancesTopComponent;
    }

    public MappingTaskTopComponent getMappingTaskTopComponent() {
        return mappingTaskTopComponent;
    }

    public TransformationTopComponent getTransformationTopComponent() {
        return transformationTopComponent;
    }

    public void setTransformationTopComponent(TransformationTopComponent transformationTopComponent) {
        this.transformationTopComponent = transformationTopComponent;
        setChanged();
        notifyObservers(transformationTopComponent);
    }

    public XQueryTopComponent getXQueryTopComponent() {
        return xQueryTopComponent;
    }

    public void setXQueryTopComponent(XQueryTopComponent xQueryTopComponent) {
        this.xQueryTopComponent = xQueryTopComponent;
        setChanged();
        notifyObservers(xQueryTopComponent);
    }

    public SqlTopComponent getSqlTopComponent() {
        return sqlTopComponent;
    }

    public void setSqlTopComponent(SqlTopComponent sqlTopComponent) {
        this.sqlTopComponent = sqlTopComponent;
        setChanged();
        notifyObservers(sqlTopComponent);
    }

    public TGDListTopComponent getTGDListTopComponent() {
        return tgdListTopComponent;
    }

    public void setTGDListTopComponent(TGDListTopComponent tgdListTopComponent) {
        this.tgdListTopComponent = tgdListTopComponent;
        setChanged();
        notifyObservers(tgdListTopComponent);
    }

    public TGDCorrespondencesTopComponent getTGDCorrespondencesTopComponent() {
        return tgdCorrespondencesTopComponent;
    }

    public void setTGDCorrespondencesTopComponent(TGDCorrespondencesTopComponent tgdCorrespondencesTopComponent) {
        this.tgdCorrespondencesTopComponent = tgdCorrespondencesTopComponent;
        setChanged();
        notifyObservers(tgdCorrespondencesTopComponent);
    }

    public SpicyTopComponent getSpicyTopComponent() {
        return spicyTopComponent;
    }

    public void setSpicyTopComponent(SpicyTopComponent spicyTopComponent) {
        this.spicyTopComponent = spicyTopComponent;
        setChanged();
        notifyObservers(spicyTopComponent);
    }

    public BestMappingsTopComponent getBestMappingsTopComponent() {
        return bestMappingsTopComponent;
    }

    public void setBestMappingsTopComponent(BestMappingsTopComponent bestMappingsTopComponent) {
        this.bestMappingsTopComponent = bestMappingsTopComponent;
        setChanged();
        notifyObservers(bestMappingsTopComponent);
    }

    public RankedTransformationsTopComponent getRankedTransformationsTopComponent() {
        return rankedTransformationsTopComponent;
    }

    public void setRankedTransformationsTopComponent(RankedTransformationsTopComponent rankedTransformationsTopComponent) {
        this.rankedTransformationsTopComponent = rankedTransformationsTopComponent;
        setChanged();
        notifyObservers(rankedTransformationsTopComponent);
    }

    public SelectedTransformationTopComponent getSelectedTransformationTopComponent() {
        return selectedTransformationTopComponent;
    }

    public void setSelectedTransformationTopComponent(SelectedTransformationTopComponent selectedTransformationTopComponent) {
        this.selectedTransformationTopComponent = selectedTransformationTopComponent;
        setChanged();
        notifyObservers(selectedTransformationTopComponent);
    }

    public CompositionTopComponent getCompositionTopComponent() {
        return compositionTopComponent;
    }

    public void setCompositionTopComponent(CompositionTopComponent compositionTopComponent) {
        this.compositionTopComponent = compositionTopComponent;
        setChanged();
        notifyObservers(compositionTopComponent);
    }*/

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getter & Setter -- altre proprietà">
    public FORule getSelectedFORule() {
        return selectedFORule;
    }

    public void setSelectedFORule(FORule selectedFORule) {
        this.selectedFORule = selectedFORule;
    }

    public String getID() {
        return ID;
    }

    public MappingTask getMappingTask() {
        return mappingTask;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Object getStato() {
        return stato;
    }

    public void setStato(Object stato) {
        this.stato = stato;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Image getImageNumber() {
        return imageNumber;
    }

    public EngineConfigurationPM getEngineConfigurationPM() {
        return engineConfigurationPM;
    }

    public boolean isTGDSession() {
        return tgdSession;
    }

    public void setTGDSession(boolean tgdSession) {
        this.tgdSession = tgdSession;
    }

//    public boolean isModified() {
//        return modified;
//    }
//
//    public void setModified(boolean modified) {
//        this.modified = modified;
//    }
//
//    public boolean isInComposition() {
//        return inComposition;
//    }
//
//    public void setInComposition(boolean inComposition) {
//        this.inComposition = inComposition;
//    }

    public List<Scenario> getRelatedScenarios() {
        return relatedScenarios;
    }

    public void setRelatedScenarios(List<Scenario> relatedScenarios) {
        this.relatedScenarios = relatedScenarios;
    }
    
    public void addRelatedScenario(Scenario scenario) {
        this.relatedScenarios.add(scenario);
    }
    // </editor-fold>
}
