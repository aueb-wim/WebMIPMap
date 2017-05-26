/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.users.recommendation.mappingmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    
    public void printSchema(){
        System.out.println("Database Name: " + databaseName);
        for(Table t: tables){
            HashMap<String, ArrayList<Field>> table = t.getTableName();
            for (Map.Entry<String, ArrayList<Field>> entry : table.entrySet()) {
                System.out.println("Table Name: " + entry.getKey());
                for(Field f : entry.getValue()){
                    System.out.println("\tAttribute Name/Type: " + f.getFieldName() + " / " + f.getFieldType());
                }
            }
        }
    }
    
}
