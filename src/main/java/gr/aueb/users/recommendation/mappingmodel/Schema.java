/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.ArrayList;
/**
 *
 * @author ioannisxar
 */
public class Schema {
    
    private ArrayList<Table> tables;
    private String databaseName;
    
    public Schema(String databaseName, ArrayList<Table> tables){
        this.databaseName = databaseName;
        this.tables = tables;
    }

    public Schema() {
        tables = new ArrayList<>();
    }
    
    public void addDatabaseName(String databaseName){
        this.databaseName = databaseName;
    }
    
    public void addTables(ArrayList<Table> tables){
        this.tables = tables;
    }
    
    public String getDatabaseName(){
        return databaseName;
    }
    
    public ArrayList<Table> getDatabaseTables(){
        return tables;
    }
    
    public ArrayList<String> getTableNames(){
        ArrayList<String> l = new ArrayList<>();
        for(Table t: tables){
            l.add(t.getTableName());
        }
        return l;
    }
    
    public ArrayList<String> getTableAttributeNames(){
        ArrayList<String> l = new ArrayList<>();
        tables.forEach((t) -> {
            String tableName = t.getTableName();
            t.getAttributes().forEach((f) -> {
                l.add(tableName+"."+f.getFieldName());
            });
        });
        return l;
    }
    
    public void printSchema(){
        System.out.println("Database Name: " + databaseName);
        for(Table t: tables){
            ArrayList<Field> attributes = t.getAttributes();
            System.out.println("Table Name: " + t.getTableName());
            for (Field f: attributes) {
                System.out.println("\tAttribute Name/Type: " + f.getFieldName() + " / " + f.getFieldType());
            }
        }
    }
    
    
    public boolean compareSchemata(MappingScenario scenario, String schemaType){
        boolean matched = true;
        Schema schema = null;
        if(schemaType.equals("source")){
            schema = scenario.getSource();
        } else if(schemaType.equals("target")) {
            schema = scenario.getTarget();
        }
        
        //checks if the database name in both schemata is same
        if(!databaseName.equals(schema.databaseName)){
            return false;
        }
        
        //checks if the table names are common in both schemata
        if(!getTableNames().containsAll(schema.getTableNames()) || !schema.getTableNames().containsAll(getTableNames())){
            return false;
        }
        
        //checks if all tables and attributes are same
        if(!tables.containsAll(schema.getDatabaseTables()) || !schema.getDatabaseTables().containsAll(tables)){
            return false;
        }      
        
        return matched;
    }
    
}
