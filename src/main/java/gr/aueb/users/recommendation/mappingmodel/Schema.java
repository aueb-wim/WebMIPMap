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
    
}
