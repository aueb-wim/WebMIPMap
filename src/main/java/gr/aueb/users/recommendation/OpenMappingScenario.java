/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation;

import au.com.bytecode.opencsv.CSVReader;
import gr.aueb.mipmapgui.controller.file.MipmapDirectories;
import gr.aueb.users.recommendation.mappingmodel.Correspondence;
import gr.aueb.users.recommendation.mappingmodel.Field;
import gr.aueb.users.recommendation.mappingmodel.Schema;
import gr.aueb.users.recommendation.mappingmodel.Table;
import it.unibas.spicy.model.correspondence.GetIdFromDb;
import it.unibas.spicy.model.datasource.INode;
import it.unibas.spicy.model.mapping.IDataSourceProxy;
import it.unibas.spicy.persistence.DAOException;
import it.unibas.spicy.persistence.sql.DAOSql;
import it.unibas.spicy.persistence.xml.DAOXmlUtility;
import it.unibas.spicy.utility.SpicyEngineConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.jdom.Document;
import org.jdom.Element;

/**
 *
 * @author ioannisxar
 */
public class OpenMappingScenario {
    
    private String user, scenarioName;
    private DAOXmlUtility daoUtility = new DAOXmlUtility();
    
    public OpenMappingScenario(String user, String scenarioName){
        this.user = user;
        this.scenarioName = scenarioName;
    }
    
    public Schema getScenarioSchema(String inputType, String mode) throws DAOException, FileNotFoundException, IOException, JSQLParserException{
        String mappingTaskFile;
        String basePath = "";
        if(mode.equals("private")){
            basePath  = MipmapDirectories.getUserPrivatePath(user) + scenarioName;
        } else if(mode.equals("public")){
            basePath  = MipmapDirectories.getUserPublicPath(user) + scenarioName;
        }
        mappingTaskFile = basePath +"/mapping_task.xml";
        Document document = daoUtility.buildDOM(mappingTaskFile);
        Element sourceElement = document.getRootElement().getChild(inputType);
        String sourceType = sourceElement.getChildText("type");
        HashMap<String, ArrayList<String>> map = mappingFilePaths(sourceElement);
        ArrayList<Table> sourceTables = new ArrayList<>();
        String databaseName = "";
        for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            databaseName = entry.getKey();
            for (String filePath : entry.getValue()) {
                sourceTables.add(getTable(basePath + "/" + filePath, sourceType));
            }
        }
        Schema schema = new Schema(databaseName, sourceTables);
        return schema;
    }
    
    public ArrayList<Correspondence> getScenarioCorrespondences() throws DAOException{
        String mappingTaskFile = MipmapDirectories.getUserPublicPath(user) + scenarioName +"/mapping_task.xml";
        Document document = daoUtility.buildDOM(mappingTaskFile);
        Element root = document.getRootElement().getChild("correspondences");
        List<Element> correspondences = root.getChildren("correspondence");
        ArrayList<Correspondence> correspondenceList = new ArrayList<>();
        for(Element correspondence: correspondences){
            String target = correspondence.getChildTextTrim("target-path");
            String function = correspondence.getChildTextTrim("transformation-function");
            if(correspondence.getChild("source-paths").getChildTextTrim("source-path") != null){
                String path = correspondence.getChild("source-paths").getChildTextTrim("source-path");
                correspondenceList.add(new Correspondence(path, target, function));
            } else if(correspondence.getChildTextTrim("source-value") != null){
                String value = correspondence.getChildTextTrim("source-value");
                if(!function.equals("newId()")){
                    correspondenceList.add(new Correspondence(value, target, function));
                } else {
                    if(value.contains("getId()")){
                        String sequence = correspondence.getChild("source-value").getChildTextTrim("sequence");
                        Element driverElement = correspondence.getChild("source-value").getChild("relational").getChild("driver");
                        Element uriElement = correspondence.getChild("source-value").getChild("relational").getChild("uri");
                        Element schemaNameElement = correspondence.getChild("source-value").getChild("relational").getChild("schema");
                        Element loginElement = correspondence.getChild("source-value").getChild("relational").getChild("login");
                        Element passwordElement = correspondence.getChild("source-value").getChild("relational").getChild("password");
                        Element tableElement = correspondence.getChild("source-value").getChild("relational").getChild("table");
                        Element columnElement = correspondence.getChild("source-value").getChild("relational").getChild("column");
                        Element functionElement = correspondence.getChild("source-value").getChild("relational").getChild("function");
                        String schema = "";
                        if (schemaNameElement != null) {
                            schema = schemaNameElement.getTextTrim();
                        }
                        GetIdFromDb newIdFromDb = new GetIdFromDb(driverElement.getTextTrim(), uriElement.getTextTrim(), schema, loginElement.getTextTrim(), 
                        passwordElement.getTextTrim(), tableElement.getTextTrim(), columnElement.getTextTrim(), functionElement.getTextTrim());
                        correspondenceList.add(new Correspondence(value, target, function, sequence, newIdFromDb));
                    } else {
                        String sequence = correspondence.getChild("source-value").getChildTextTrim("sequence");
                        String offset = correspondence.getChild("source-value").getChildTextTrim("offset");
                        correspondenceList.add(new Correspondence(value, target, function, sequence, offset));
                    }
                }     
            }
        }
        return correspondenceList;
    }
    
    private HashMap<String, ArrayList<String>> mappingFilePaths(Element element){
        ArrayList<String> filePaths = new ArrayList<>();
        String type = element.getChildText("type");
        Element typeElement = element.getChild(type.toLowerCase());
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        if(type.equalsIgnoreCase(SpicyEngineConstants.TYPE_CSV)){
            Element tableElement = typeElement.getChild(type.toLowerCase()+"-tables");
            List<Element> tableElements = tableElement.getChildren();
            String databaseName = typeElement.getChildText(type.toLowerCase() + "-db-name");
            for (Element e: tableElements){
                filePaths.add(e.getChildText("schema"));
            }
            map.put(databaseName, filePaths);
        } else if(type.equalsIgnoreCase(SpicyEngineConstants.TYPE_SQL)){
             String databaseName = typeElement.getChildText(type.toLowerCase() + "-db-name");
             String file = typeElement.getChildText("sql-file");
             filePaths.add(file);
             map.put(databaseName, filePaths);
        }
        return map;
    }
    
    private Table getTable(String filePath, String inputType) throws FileNotFoundException, IOException, DAOException, JSQLParserException{
        ArrayList<Field> fieldList = new ArrayList<>();
        Table t = null;
        //Get schema from CSV
        if(inputType.equals(SpicyEngineConstants.TYPE_CSV)){
            CSVReader reader = new CSVReader(new FileReader(filePath));
            for(String attribute : reader.readNext()){
                fieldList.add(new Field(attribute, "String"));
            }
            File f = new File(filePath);
            t = new Table(f.getName().split("\\.")[0], fieldList);
        } // Get schema from SQL
        else if(inputType.equals(SpicyEngineConstants.TYPE_SQL)){
            String sqlScript = readFile(filePath, StandardCharsets.UTF_8).trim();            
            Statements stmts = CCJSqlParserUtil.parseStatements(sqlScript);
            List<net.sf.jsqlparser.statement.Statement> stmtss = stmts.getStatements();
            for (net.sf.jsqlparser.statement.Statement stmt: stmtss){
                if (stmt instanceof CreateTable){
                    CreateTable createStmt = (CreateTable) stmt;
                    String tableName = createStmt.getTable().getName(); 
                    System.out.println(tableName);
                    List<ColumnDefinition> columns = createStmt.getColumnDefinitions();
                    for(ColumnDefinition cd: columns){
                        System.out.println(cd.getColumnName());
                    }
                }
            }
        }
        return t;
    }
    
    static String readFile(String path, Charset encoding) throws IOException {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, encoding);
    }
    
}
