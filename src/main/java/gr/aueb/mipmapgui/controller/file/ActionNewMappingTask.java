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

import it.unibas.spicy.model.datasource.ForeignKeyConstraint;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.model.mapping.MappingTask;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.relational.DAORelational;
import it.unibas.spicy.persistence.relational.DBFragmentDescription;
import it.unibas.spicy.persistence.relational.IConnectionFactory;
import it.unibas.spicy.persistence.relational.SimpleDbConnectionFactory;
import it.unibas.spicy.persistence.xml.DAOXsd;
import it.unibas.spicy.persistence.csv.DAOCsv;
import it.unibas.spicy.persistence.sql.DAOSql;
import it.unibas.spicy.utility.SpicyEngineConstants;
import gr.aueb.mipmapgui.Costanti;
import it.unibas.spicygui.commons.AbstractScenario;
import it.unibas.spicygui.commons.Modello;
import gr.aueb.mipmapgui.controller.Scenarios;
import gr.aueb.mipmapgui.controller.Scenario;
import gr.aueb.mipmapgui.controller.datasource.operators.JSONTreeCreator;
import gr.aueb.mipmapgui.view.wizard.pm.NewMappingTaskPM;
import gr.aueb.mipmapgui.view.wizard.pm.XMLConfigurationPM;
import gr.aueb.mipmapgui.view.wizard.pm.RelationalConfigurationPM;
import gr.aueb.mipmapgui.view.wizard.pm.CSVConfigurationPM;
import gr.aueb.mipmapgui.view.wizard.pm.SQLConfigurationPM;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;

public final class ActionNewMappingTask{

    private Modello modello;
    private DAOXsd daoXsd = new DAOXsd();
    private DAOCsv daoCsv = new DAOCsv();
    private DAOSql daoSql = new DAOSql();
    private DAORelational daoRelational = new DAORelational();
    private int scenarioNo;
    private JSONObject treeObject = new JSONObject();
    //folder path where the server uploads the Source files
    private String sourceDir;
    //folder path where the server uploads the Target files
    private String targetDir;
    //folder path where the saved schema files are uploaded on the server
    private String savedDir;
    
    
    public ActionNewMappingTask(Modello modello, String user) {
        this.modello = modello;        
        this.sourceDir = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user+ "/" + Costanti.SERVER_TEMP_FOLDER + Costanti.SERVER_SOURCE_FOLDER;
        this.targetDir = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_FILES_FOLDER + user+ "/" + Costanti.SERVER_TEMP_FOLDER + Costanti.SERVER_TARGET_FOLDER;
        this.savedDir = Costanti.SERVER_MAIN_FOLDER + Costanti.SERVER_SCHEMATA_FOLDER;
    }
    
    public void performAction(HttpServletRequest request) throws DAOException {
        insertBeanForBinding();
        
        this.scenarioNo = Integer.valueOf(request.getParameter("scenarioNo"));
        
        NewMappingTaskPM newMappingTaskPM = (NewMappingTaskPM) this.modello.getBean(Costanti.NEW_MAPPING_TASK_PM);

        newMappingTaskPM.setSourceElement(request.getParameter("typeSource"));
        newMappingTaskPM.setTargetElement(request.getParameter("typeTarget"));

        if (request.getParameter("typeSource").equalsIgnoreCase("csv")){
            createCSVConfiguration(true, request.getParameter("dbnameSource"), request.getParameterValues("filesSource[]"));                                   
        }
        else if(request.getParameter("typeSource").equalsIgnoreCase("xml")){
           createXMLConfiguration(true, request.getParameter("xmlSource"));
        }
        else if(request.getParameter("typeSource").equalsIgnoreCase("sql")){
           createSQLConfiguration(true, false, request.getParameter("sqlDbNameSource"), request.getParameter("sqlSource"));          
        }
        else if(request.getParameter("typeSource").equalsIgnoreCase("saved")){
           createSQLConfiguration(true, true, request.getParameter("savedDBNameSource"), request.getParameter("savedDBNameSource")+".sql");
        } 
        /*else if(request.getParameter("typeSource").equalsIgnoreCase("relational")){
           createRelationalConfiguration(true, request.getParameter("driverSource"), request.getParameter("uriSource"), 
                   request.getParameter("usernameSource"), request.getParameter("passwordSource"));
        }*/
        if (request.getParameter("typeTarget").equalsIgnoreCase("csv")){
            createCSVConfiguration(false, request.getParameter("dbnameTarget"), request.getParameterValues("filesTarget[]"));                                       
        } 
        else if(request.getParameter("typeTarget").equalsIgnoreCase("xml")){
           createXMLConfiguration(false, request.getParameter("xmlTarget"));
        }
        else if(request.getParameter("typeTarget").equalsIgnoreCase("sql")){
           createSQLConfiguration(false, false, request.getParameter("sqlDbNameTarget"), request.getParameter("sqlTarget"));
        }  
        else if(request.getParameter("typeTarget").equalsIgnoreCase("saved")){
           createSQLConfiguration(false, true, request.getParameter("savedDBNameTarget"), request.getParameter("savedDBNameTarget")+".sql");
        } 
        /*else if(request.getParameter("typeTarget").equalsIgnoreCase("relational")){
           createRelationalConfiguration(false, request.getParameter("driverTarget"), request.getParameter("uriTarget"), 
                   request.getParameter("usernameTarget"), request.getParameter("passwordTarget"));
        }*/

        IDataSourceProxy source = loadDataSource(newMappingTaskPM.getSourceElement(), true);
        IDataSourceProxy target = loadDataSource(newMappingTaskPM.getTargetElement(), false);

        MappingTask mappingTask = new MappingTask(source, target, SpicyEngineConstants.LINES_BASED_MAPPING_TASK);

        if (!source.getForeignKeyConstraints().isEmpty()) {
            addForeignKeyToJoin(source);
        }
        if (!target.getForeignKeyConstraints().isEmpty()) {
            addForeignKeyToJoin(target);
        }

        gestioneScenario(mappingTask);

        JSONTreeCreator treeCreator = new JSONTreeCreator(modello);
        this.treeObject = treeCreator.createSchemaTrees();
    }
    
    private void createCSVConfiguration(boolean source, String DBName, String[] CSVfiles){
        if (source){
            CSVConfigurationPM sourceCSVConfigurationPM = (CSVConfigurationPM) this.modello.getBean(Costanti.CSV_CONFIGURATION_SOURCE);
            sourceCSVConfigurationPM.setDBName(DBName);
            for (String subPath : CSVfiles) {
                sourceCSVConfigurationPM.addToSchemaPathList(sourceDir + subPath);
            }
        }
        else{
            CSVConfigurationPM targetCSVConfigurationPM = (CSVConfigurationPM) this.modello.getBean(Costanti.CSV_CONFIGURATION_TARGET);
            targetCSVConfigurationPM.setDBName(DBName);
            for (String subPath : CSVfiles) {
                targetCSVConfigurationPM.addToSchemaPathList(targetDir + subPath);
            }
        }
    }
    
    private void createXMLConfiguration(boolean source, String XMLfile){
        if (source){
            XMLConfigurationPM sourceXMLConfigurationPM = (XMLConfigurationPM) this.modello.getBean(Costanti.XML_CONFIGURATION_SOURCE);
            sourceXMLConfigurationPM.setSchemaPath(sourceDir + XMLfile);
        }
        else{
            XMLConfigurationPM targetXMLConfigurationPM = (XMLConfigurationPM) this.modello.getBean(Costanti.XML_CONFIGURATION_TARGET);
            targetXMLConfigurationPM.setSchemaPath(targetDir + XMLfile);
        }
    }
    
    private void createSQLConfiguration(boolean source, boolean saved, String DBName, String SQLfile){
        String path;
        if (saved)
            path = savedDir;        
        else
            if(source)
                path = sourceDir;
            else
                path = targetDir;        
        if (source){
            SQLConfigurationPM sourceSQLConfigurationPM = (SQLConfigurationPM) this.modello.getBean(Costanti.SQL_CONFIGURATION_SOURCE);
            sourceSQLConfigurationPM.setDBName(DBName);
            sourceSQLConfigurationPM.setSchemaPath(path + SQLfile);
        }
        else{
            SQLConfigurationPM targetSQLConfigurationPM = (SQLConfigurationPM) this.modello.getBean(Costanti.SQL_CONFIGURATION_TARGET);
            targetSQLConfigurationPM.setDBName(DBName);
            targetSQLConfigurationPM.setSchemaPath(path + SQLfile);
        }
    }
        
        
    private void createRelationalConfiguration(boolean source, String driver, String uri, String username, String password){
        if(source){
            RelationalConfigurationPM sourceRelationConfigurationPM = (RelationalConfigurationPM) this.modello.getBean(Costanti.RELATIONAL_CONFIGURATION_SOURCE);
            sourceRelationConfigurationPM.setDriver(driver);
            sourceRelationConfigurationPM.setUri(uri);
            sourceRelationConfigurationPM.setLogin(username);
            sourceRelationConfigurationPM.setPassword(password);
        }
        else{
            RelationalConfigurationPM targetRelationConfigurationPM = (RelationalConfigurationPM) this.modello.getBean(Costanti.RELATIONAL_CONFIGURATION_TARGET);           
            targetRelationConfigurationPM.setDriver(driver);
            targetRelationConfigurationPM.setUri(uri);
            targetRelationConfigurationPM.setLogin(username);
            targetRelationConfigurationPM.setPassword(password);
        }
    }

    private void addForeignKeyToJoin(IDataSourceProxy dataSource) {
        List<ForeignKeyConstraint> foreignKeyConstraints = new ArrayList<ForeignKeyConstraint>(dataSource.getForeignKeyConstraints());
        for (ForeignKeyConstraint foreignKey : foreignKeyConstraints) {
            dataSource.addJoinForForeignKey(foreignKey);
        } 
    }

    private void gestioneScenario(MappingTask mappingTask) {
        Scenarios scenarios = (Scenarios) modello.getBean(Costanti.SCENARIOS);
        if (scenarios == null) {
            scenarios = new Scenarios("New Mapping Task Scenarios");
            modello.putBean(Costanti.SCENARIOS, scenarios);
            ////actionProjectTree.performAction();
        }
        AbstractScenario scenario = new Scenario("New Mapping Task Scenario "+scenarioNo, mappingTask, true);
        scenarios.addScenario(scenario);
        Scenario scenarioOld = (Scenario) modello.getBean(Costanti.CURRENT_SCENARIO);
        if (scenarioOld != null) {
            scenarioOld.setSelected(false);
        }         
        //web
        HashMap<Integer, Scenario> scenarioMap = (HashMap) modello.getBean(Costanti.SCENARIO_MAPPER);
        scenarioMap.put(scenarioNo, (Scenario) scenario);
        modello.putBean(Costanti.CURRENT_SCENARIO, scenario);
    }

    private IDataSourceProxy loadDataSource(String type, boolean source) throws DAOException {
       if (type.equalsIgnoreCase("relational")) {
            if (source) {
                return loadRelationalDataSource((RelationalConfigurationPM) modello.getBean(Costanti.RELATIONAL_CONFIGURATION_SOURCE), true);
            }
            return loadRelationalDataSource((RelationalConfigurationPM) modello.getBean(Costanti.RELATIONAL_CONFIGURATION_TARGET), false);
        }
       if (type.equalsIgnoreCase("sql") || type.equalsIgnoreCase("saved") ) {
            if (source) {
                return loadSQLDataSource((SQLConfigurationPM) modello.getBean(Costanti.SQL_CONFIGURATION_SOURCE), true);
            }
            return loadSQLDataSource((SQLConfigurationPM) modello.getBean(Costanti.SQL_CONFIGURATION_TARGET), false);
        }
        else if (type.equalsIgnoreCase("xml")) {
            if (source) {
                return loadXMLDataSource((XMLConfigurationPM) modello.getBean(Costanti.XML_CONFIGURATION_SOURCE));
            }
            return loadXMLDataSource((XMLConfigurationPM) modello.getBean(Costanti.XML_CONFIGURATION_TARGET));
        }
        if (source) {
            return loadCSVDataSource((CSVConfigurationPM) modello.getBean(Costanti.CSV_CONFIGURATION_SOURCE));
        }
        return loadCSVDataSource((CSVConfigurationPM) modello.getBean(Costanti.CSV_CONFIGURATION_TARGET)); 
    }

    private IDataSourceProxy loadRelationalDataSource(RelationalConfigurationPM configuration, boolean source) throws DAOException {
        DBFragmentDescription dataDescription = new DBFragmentDescription();
        IConnectionFactory dataSourceDB = new SimpleDbConnectionFactory();
        IDataSourceProxy dataSource = daoRelational.loadSchemaForWeb(scenarioNo, configuration.getAccessConfiguration(), dataDescription, dataSourceDB, source);
        return dataSource;
    }

    private IDataSourceProxy loadXMLDataSource(XMLConfigurationPM configuration) throws DAOException {
        IDataSourceProxy dataSource = daoXsd.loadSchema(configuration.getSchemaPath());
        return dataSource;
    }
    
    private IDataSourceProxy loadSQLDataSource(SQLConfigurationPM configuration, boolean source) throws DAOException {
        IDataSourceProxy dataSource = daoSql.loadSchemaForWeb(scenarioNo, configuration.getDBName(), configuration.getSchemaPath(), source);
        return dataSource;
    }
    
    private IDataSourceProxy loadCSVDataSource(CSVConfigurationPM configuration) throws DAOException {
        IDataSourceProxy dataSource = daoCsv.loadSchemaForWeb(scenarioNo, configuration.getSchemaPathList(), configuration.getDBName());
        return dataSource;        
    }

    public String getName() {
        return Costanti.ACTION_NEW;
    }
    
    public JSONObject getSchemaTreesObject(){
        return this.treeObject;
    }

    private void insertBeanForBinding() {
        this.modello.putBean(Costanti.RELATIONAL_CONFIGURATION_SOURCE, new RelationalConfigurationPM());
        this.modello.putBean(Costanti.RELATIONAL_CONFIGURATION_TARGET, new RelationalConfigurationPM());
        this.modello.putBean(Costanti.XML_CONFIGURATION_SOURCE, new XMLConfigurationPM());
        this.modello.putBean(Costanti.XML_CONFIGURATION_TARGET, new XMLConfigurationPM());
        this.modello.putBean(Costanti.SQL_CONFIGURATION_SOURCE, new SQLConfigurationPM());
        this.modello.putBean(Costanti.SQL_CONFIGURATION_TARGET, new SQLConfigurationPM());
        this.modello.putBean(Costanti.CSV_CONFIGURATION_SOURCE, new CSVConfigurationPM());
        this.modello.putBean(Costanti.CSV_CONFIGURATION_TARGET, new CSVConfigurationPM());
        this.modello.putBean(Costanti.NEW_MAPPING_TASK_PM, new NewMappingTaskPM());
    }  
    
}
